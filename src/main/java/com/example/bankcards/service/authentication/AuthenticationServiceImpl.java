package com.example.bankcards.service.authentication;

import com.example.bankcards.dto.JwtAuthenticationResponse;
import com.example.bankcards.dto.SignInRequest;
import com.example.bankcards.dto.SignUpRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.user.InternalUserService;
import com.example.bankcards.util.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager tokenManager;
    private final InternalUserService userService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = tokenManager.generateJwtToken(user);
        return new JwtAuthenticationResponse(token);
    }

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        UserDto userDto = new UserDto(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        User user = userService.createUser(userDto);
        String token = tokenManager.generateJwtToken(new CustomUserDetails(user));
        return new JwtAuthenticationResponse(token);
    }
}
