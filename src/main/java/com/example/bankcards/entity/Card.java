package com.example.bankcards.entity;

import com.example.bankcards.entity.enums.CardState;
import com.example.bankcards.util.CardEncryptor;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "cards")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card extends BaseDomain {

    @Column(name = "card_number")
    @Convert(converter = CardEncryptor.class)
    String cardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @Column(name = "validity_period")
    Instant validityPeriod;

    @Column(name = "balance")
    Float balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_state")
    CardState state;
}
