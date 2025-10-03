package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class InternalUserServiceImpl implements InternalUserService {

    private final static long USER_ROLE_ID = 1;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login " + username + " not found"));
    }

    @Override
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }

    @Override
    public User createUser(UserDto userDto) {
        String username = userDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("User with name " + username + " already exists");
        }
        Role userRole = roleRepository.findById(USER_ROLE_ID)
                .orElseThrow(() -> new NotFoundException("Роль с id " + USER_ROLE_ID + "не найдена"));
        User user = new User();
        user.setUsername(username);
        user.setPassword(userDto.getPassword());
        user.setRole(userRole);
        return userRepository.save(user);
    }

}
