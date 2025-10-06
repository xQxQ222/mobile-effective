package com.example.bankcards.config;

import com.example.bankcards.service.user.InternalUserService;
import com.example.bankcards.util.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";

    private final JwtTokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HEADER_NAME);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            String jwt = authorization.substring(BEARER_PREFIX.length());
            try {
                String username = tokenManager.getUsername(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            tokenManager.getRoles(jwt).stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (MalformedJwtException exception) {
                log.error("Error: {}", exception.getMessage());
            } catch (ExpiredJwtException exception) {
                log.error("Error: {}", exception.getMessage());
            } catch (SignatureException exception) {
                log.error("Error: {}", exception.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}

