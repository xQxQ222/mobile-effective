package com.example.bankcards.repository;

import com.example.bankcards.entity.BlockRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRequestRepository extends JpaRepository<BlockRequest, Long> {
    Optional<BlockRequest> findByRequesterIdAndCardId(Long requesterId, Long cardId);
}
