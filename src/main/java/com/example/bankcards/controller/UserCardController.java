package com.example.bankcards.controller;

import com.example.bankcards.dto.BlockCardRequestDto;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.cards.personal.PersonalCardService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards/my")
@Slf4j
@RequiredArgsConstructor
public class UserCardController {

    private final PersonalCardService cardService;

    @GetMapping
    public Page<CardDto> getAllMyCards(@PageableDefault(sort = "balance") Pageable pageable) {
        log.info("Hit GET /cards/my");
        return cardService.getMyCards(pageable);
    }

    @PatchMapping("/{cardId}/block")
    public BlockCardRequestDto blockCard(@PathVariable(name = "cardId") Long cardId) {
        log.info("Hit PATCH /cards/my/{}/block", cardId);
        return cardService.requestBlockCard(cardId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public BlockCardRequestDto cancelRequestBlock(@PathVariable(name = "requestId") Long requestId) {
        log.info("Hit PATCH /cards/my/requests/{}/cancel", requestId);
        return cardService.cancelRequestBlock(requestId);
    }

    @PutMapping("/transfer")
    public void transferBalance(@RequestParam(name = "cardIdFrom") Long cardIdFrom, @RequestParam(name = "cardIdTo") Long cardIdTo, @RequestBody @Positive float value) {
        log.info("Hit PUT /cards/my/transfer?cardIdFrom={}&cardIdTo={}", cardIdFrom, cardIdTo);
        cardService.transferBalanceToOtherCard(cardIdFrom, cardIdTo, value);
    }

    @GetMapping("/{cardId}/balance")
    public float getCardBalance(@PathVariable(name = "cardId") Long cardId) {
        log.info("Hit GET /cards/my/{}/balance", cardId);
        return cardService.getCardBalance(cardId);
    }
}
