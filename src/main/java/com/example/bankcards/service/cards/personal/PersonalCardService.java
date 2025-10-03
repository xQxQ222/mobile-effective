package com.example.bankcards.service.cards.personal;

import com.example.bankcards.dto.BlockCardRequestDto;
import com.example.bankcards.dto.CardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonalCardService {
    Page<CardDto> getMyCards(Pageable pageable);

    BlockCardRequestDto requestBlockCard(Long cardId);

    BlockCardRequestDto cancelRequestBlock(Long requestId);

    void transferBalanceToOtherCard(Long cardIdFrom, Long cardIdTo, float transferValue);

    Float getCardBalance(Long cardId);
}
