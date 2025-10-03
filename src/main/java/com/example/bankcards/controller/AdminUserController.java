package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.service.user.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final AdminUserService userService;

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(name = "userId") Long userId) {
        log.info("Hit DELETE /admin/users/{}", userId);
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}/change-status")
    public UserDto updateUserToAdmin(@PathVariable(name = "userId") Long userId) {
        log.info("Hit PATCH /admin/users/{}/change-status", userId);
        return userService.setUserAdmin(userId);
    }
}
