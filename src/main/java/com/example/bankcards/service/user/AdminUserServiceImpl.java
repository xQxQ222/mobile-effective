package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        log.info("Deleting user with id {}", userId);
        userRepository.delete(user);
    }

    @Override
    public UserDto setUserAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        log.info("Set user with id {} to admin role", userId);
        Role adminRole = roleRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Role with id " + 2 + " not found"));
        user.setRole(adminRole);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
