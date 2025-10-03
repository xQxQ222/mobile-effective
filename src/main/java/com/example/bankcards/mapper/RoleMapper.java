package com.example.bankcards.mapper;

import com.example.bankcards.dto.RoleDto;
import com.example.bankcards.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    RoleDto toDto(Role role);
}
