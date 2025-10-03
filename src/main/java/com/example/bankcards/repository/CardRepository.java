package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Modifying
    @Query("UPDATE Card c SET c.state = 'EXPIRED' WHERE c.validityPeriod < :now AND c.state <> 'EXPIRED'")
    void updateExpiredCards(Instant now);

    Page<Card> findByOwnerId(Long ownerId, Pageable pageable);
}
