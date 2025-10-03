package com.example.bankcards.util;

import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CardScheduler {

    private final CardRepository cardRepository;

    @Transactional
    @Scheduled(fixedRate = 86_400_000)
    public void updateExpiredCards() {
        Instant now = Instant.now();
        cardRepository.updateExpiredCards(now);
    }
}
