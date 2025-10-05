package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.CardState;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardDto {

    String cardNumber;

    UserDto owner;

    Instant validityPeriod;

    Float balance;

    CardState state;

    public String getCardNumber() {
        String lastQuartet = cardNumber.trim().substring(cardNumber.length() - 4);
        return "**** **** **** " + lastQuartet;
    }
}
