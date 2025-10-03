package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.cards.admin.AdminCardService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/cards")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCardController {

    private final AdminCardService cardService;

    @PostMapping("/create")
    public CardDto createNewCard(@RequestParam Long userId) {
        log.info("Hit POST /admin/cards/create");
        return cardService.generateNewCard(userId);
    }

    @PatchMapping("/block")
    public CardDto blockCard(@RequestParam(name = "requestId") Long blockCardRequestId) {
        log.info("Hit PATCH /admin/cards/block?requestId={}", blockCardRequestId);
        return cardService.blockCard(blockCardRequestId);
    }

    @PatchMapping("/block/reject")
    public void rejectBlockCard(@RequestParam(name = "requestId") Long blockCardRequestId) {
        log.info("Hit PATCH /admin/cards/block/reject?requestId={}", blockCardRequestId);
        cardService.rejectBlockCard(blockCardRequestId);
    }

    @PatchMapping("/{cardId}/activate")
    public CardDto activateCard(@PathVariable(name = "cardId") Long cardId) {
        log.info("Hit PATCH /admin/cards/activate/{}", cardId);
        return cardService.activateCard(cardId);
    }

    @DeleteMapping("/{cardId}")
    public void deleteCard(@PathVariable(name = "cardId") Long cardId) {
        log.info("Hit DELETE /admin/cards/{}", cardId);
        cardService.deleteCard(cardId);
    }

    @GetMapping
    public Page<CardDto> getAllCards(@PageableDefault(sort = "validityPeriod", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Hit GET /admin/cards");
        return cardService.getAllCards(pageable);
    }

    @GetMapping("/{cardId}")
    public CardDto getCardById(@PathVariable(name = "cardId") Long cardId) {
        log.info("Hit GET /admin/cards/{}", cardId);
        return cardService.getCardById(cardId);
    }

    @PutMapping("/{cardId}")
    public CardDto topCard(@PathVariable(name = "cardId") Long cardId, @RequestBody @Positive Float deposit) {
        log.info("Hit PUT /admin/cards/{}", cardId);
        return cardService.topCard(cardId, deposit);
    }
}
