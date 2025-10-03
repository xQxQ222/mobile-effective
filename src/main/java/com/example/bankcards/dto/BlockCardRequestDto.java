package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.RequestState;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockCardRequestDto {
    Long requesterId;
    Long cardId;
    RequestState state;
}
