package com.example.bankcards.service.cards.admin;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardState;
import com.example.bankcards.entity.enums.RequestState;
import com.example.bankcards.exception.IllegalCardStateException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminCardServiceImpl implements AdminCardService {

    private static final int VALIDITY_DURATION = 365 * 5; // Для примера 5 лет

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final BlockRequestRepository blockRequestRepository;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public CardDto generateNewCard(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        String cardNumber = CardGenerator.generateCardNumber("4", 16);
        Card newCard = Card.builder()
                .balance(0.00f)
                .cardNumber(cardNumber)
                .owner(user)
                .state(CardState.ACTIVE)
                .validityPeriod(Instant.now().plus(VALIDITY_DURATION, ChronoUnit.DAYS))
                .build();
        cardRepository.save(newCard);
        log.info("Generated new card with number {}", cardNumber);
        return cardMapper.toDto(newCard);
    }

    @Override
    @Transactional
    public CardDto blockCard(Long blockCardRequestId) {
        BlockRequest blockRequest = blockRequestRepository.findById(blockCardRequestId)
                .orElseThrow(() -> new NotFoundException("Block card request with id " + blockCardRequestId + " not found"));
        if (blockRequest.getCard().getState() != CardState.ACTIVE) {
            log.error("To block a card, it must be active");
            throw new IllegalCardStateException("To block a card, it must be active");
        }
        blockRequest.setState(RequestState.APPROVED);
        blockRequestRepository.save(blockRequest);

        Card card = cardRepository.findById(blockRequest.getCard().getId())
                .orElseThrow(() -> new NotFoundException("Card with id " + blockRequest.getCard().getId() + " not found"));
        card.setState(CardState.BLOCKED);
        cardRepository.save(card);
        log.info("Blocked card with id {}", blockRequest.getCard().getId());
        return cardMapper.toDto(card);
    }

    @Override
    public void rejectBlockCard(Long blockCardRequestId) {
        BlockRequest blockRequest = blockRequestRepository.findById(blockCardRequestId)
                .orElseThrow(() -> new NotFoundException("Block card request with id " + blockCardRequestId + " not found"));
        if (blockRequest.getCard().getState() != CardState.ACTIVE) {
            log.error("To block a card, it must be active");
            throw new IllegalCardStateException("To block a card, it must be active");
        }
        blockRequest.setState(RequestState.REJECTED);
        blockRequestRepository.save(blockRequest);
        log.info("Reject blocking card with id {}", blockRequest.getCard().getId());
    }

    @Override
    @Transactional
    public CardDto activateCard(long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardId + " not found"));
        if (card.getState() != CardState.BLOCKED) {
            log.error("To activate a card, it must be blocked");
            throw new IllegalCardStateException("To activate a card, it must be blocked");
        }
        card.setState(CardState.ACTIVE);
        cardRepository.save(card);
        log.info("Card with id {} now is active", cardId);
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional
    public void deleteCard(long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardId + " not found"));
        log.info("Delete card with id {}", cardId);
        cardRepository.delete(card);
    }

    @Override
    public Page<CardDto> getAllCards(Pageable pageable) {
        log.info("Getting all cards");
        return cardRepository.findAll(pageable)
                .map(cardMapper::toDto);
    }

    @Override
    public CardDto getCardById(long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardId + " not found"));
        log.info("Getting info about card with id {}", cardId);
        return cardMapper.toDto(card);
    }

    @Override
    public CardDto topCard(long cardId, float deposit) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardId + " not found"));
        if (card.getState() != CardState.ACTIVE) {
            log.error("It is impossible to top up an inactive card");
            throw new IllegalCardStateException("It is impossible to top up an inactive card");
        }
        float currentBalance = card.getBalance();
        card.setBalance(currentBalance + deposit);
        cardRepository.save(card);
        log.info("Top card with id {} to balance: {}", card.getId(), card.getBalance());
        return cardMapper.toDto(card);
    }
}
