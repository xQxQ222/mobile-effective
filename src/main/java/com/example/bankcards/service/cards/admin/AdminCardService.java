package com.example.bankcards.service.cards.admin;

import com.example.bankcards.dto.CardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCardService {
    CardDto generateNewCard(long userId);

    CardDto blockCard(Long blockCardRequestId);

    void rejectBlockCard(Long blockCardRequestId);

    CardDto activateCard(long cardId);

    void deleteCard(long cardId);

    Page<CardDto> getAllCards(Pageable pageable);

    CardDto getCardById(long cardId);

    CardDto topCard(long cardId, float deposit);
}
