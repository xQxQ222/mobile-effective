package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface InternalUserService {
    User getUserByUsername(String username);

    User getCurrentUser();

    User createUser(UserDto userDto);
}
