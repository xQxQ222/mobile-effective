package com.example.bankcards.mapper;

import com.example.bankcards.dto.BlockCardRequestDto;
import com.example.bankcards.entity.BlockRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BlockRequestMapper {
    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "card.id", target = "cardId")
    BlockCardRequestDto toDto(BlockRequest request);
}
