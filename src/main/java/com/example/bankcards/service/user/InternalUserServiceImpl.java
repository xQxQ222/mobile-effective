package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InternalUserServiceImpl implements InternalUserService {

    private final static long USER_ROLE_ID = 1;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User getUserByUsername(String username) {
        log.info("Getting info about user with username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login " + username + " not found"));
    }

    @Override
    public User getCurrentUser() {
        log.info("Getting info about current user");
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }

    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        String username = userDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("User with name " + username + " already exists");
        }
        Role userRole = roleRepository.findById(USER_ROLE_ID)
                .orElseThrow(() -> new NotFoundException("Роль с id " + USER_ROLE_ID + "не найдена"));
        User user = User.builder()
                .username(username)
                .password(userDto.getPassword())
                .role(userRole)
                .build();
        log.info("Creating new user with username {}", username);
        return userRepository.save(user);
    }

}
