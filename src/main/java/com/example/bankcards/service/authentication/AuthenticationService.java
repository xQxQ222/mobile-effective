package com.example.bankcards.service.authentication;

import com.example.bankcards.dto.JwtAuthenticationResponse;
import com.example.bankcards.dto.SignInRequest;
import com.example.bankcards.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signIn(SignInRequest request);

    JwtAuthenticationResponse signUp(SignUpRequest request);
}
