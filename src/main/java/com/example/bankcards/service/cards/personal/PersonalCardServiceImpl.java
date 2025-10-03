package com.example.bankcards.service.cards.personal;

import com.example.bankcards.dto.BlockCardRequestDto;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardState;
import com.example.bankcards.entity.enums.RequestState;
import com.example.bankcards.exception.*;
import com.example.bankcards.mapper.BlockRequestMapper;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.InternalUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PersonalCardServiceImpl implements PersonalCardService {

    private final CardRepository cardRepository;
    private final InternalUserService internalUserService;
    private final CardMapper cardMapper;
    private final UserRepository userRepository;
    private final BlockRequestRepository blockRequestRepository;
    private final BlockRequestMapper blockRequestMapper;

    @Override
    public Page<CardDto> getMyCards(Pageable pageable) {
        log.info("Getting info about all cards of user with id {}", getCurrentUserId());
        return cardRepository.findByOwnerId(getCurrentUserId(), pageable)
                .map(cardMapper::toDto);
    }

    @Override
    @Transactional
    public BlockCardRequestDto requestBlockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardId + " not found"));
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User with id " + getCurrentUserId() + " not found"));
        if (!card.getOwner().getId().equals(user.getId())) {
            log.error("Not an owner of a card");
            throw new NotCardOwnerException(user.getId(), cardId);
        }
        if (card.getState() != CardState.ACTIVE) {
            log.error("To block a card, it must be active");
            throw new IllegalCardStateException("To block a card, it must be active");
        }
        BlockRequest blockRequest = BlockRequest.builder()
                .requester(user)
                .state(RequestState.PENDING)
                .card(card)
                .build();
        blockRequestRepository.save(blockRequest);
        log.info("Creating a blocking card request");
        return blockRequestMapper.toDto(blockRequest);
    }

    @Override
    public BlockCardRequestDto cancelRequestBlock(Long requestId) {
        BlockRequest blockRequest = blockRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id " + requestId + " not found"));
        if (!blockRequest.getRequester().getId().equals(getCurrentUserId())) {
            log.error("Not an initiator of request");
            throw new NotRequestInitiatorException(getCurrentUserId(), requestId);
        }
        if (blockRequest.getState() != RequestState.PENDING) {
            log.error("Incorrect request state: {}", blockRequest.getState().name());
            throw new IllegalRequestStateException("Request must be pending to cancel it");
        }
        blockRequest.setState(RequestState.CANCELLED);
        blockRequestRepository.save(blockRequest);
        log.info("Cancelled request(id: {})", requestId);
        return blockRequestMapper.toDto(blockRequest);
    }

    @Override
    @Transactional
    public void transferBalanceToOtherCard(Long cardIdFrom, Long cardIdTo, float transferValue) {
        Card cardFrom = cardRepository.findById(cardIdFrom)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardIdFrom + " not found"));
        Card cardTo = cardRepository.findById(cardIdTo)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardIdTo + " not found"));
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User with id " + getCurrentUserId() + " not found"));
        if (!cardFrom.getOwner().getId().equals(getCurrentUserId())) {
            throw new NotCardOwnerException(user.getId(), cardIdFrom);
        }
        if (!cardTo.getOwner().getId().equals(getCurrentUserId())) {
            throw new NotCardOwnerException(user.getId(), cardIdTo);
        }
        Float cardFromBalance = cardFrom.getBalance();
        if (cardFromBalance < transferValue) {
            throw new BalanceInsufficientException(cardIdFrom);
        }
        Float cardToBalance = cardTo.getBalance();
        cardFrom.setBalance(cardFromBalance - transferValue);
        cardRepository.save(cardFrom);

        cardTo.setBalance(cardToBalance + transferValue);
        cardRepository.save(cardTo);
    }

    @Override
    public Float getCardBalance(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardId + " not found"));
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User with id " + getCurrentUserId() + " not found"));
        if (!card.getOwner().getId().equals(user.getId())) {
            throw new NotCardOwnerException(user.getId(), cardId);
        }
        return card.getBalance();
    }

    private Long getCurrentUserId() {
        User currentUser = internalUserService.getCurrentUser();
        return currentUser.getId();
    }
}
