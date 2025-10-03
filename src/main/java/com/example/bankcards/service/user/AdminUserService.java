package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;

public interface AdminUserService {
    void deleteUser(Long userId);

    UserDto setUserAdmin(Long userId);
}
