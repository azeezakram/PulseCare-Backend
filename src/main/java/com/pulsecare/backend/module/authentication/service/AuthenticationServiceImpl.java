package com.pulsecare.backend.module.authentication.service;

import com.pulsecare.backend.module.authentication.dto.LoginRequestDTO;
import com.pulsecare.backend.module.authentication.dto.LoginResponseDTO;
import com.pulsecare.backend.module.authentication.utils.JwtUtil;
import com.pulsecare.backend.module.user.exception.UserInvalidCredentialException;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO data) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            data.username(),
                            data.password()
                    )
            );

            Users user = userRepository.findByUsername(data.username())
                    .orElseThrow(
                            () -> new UserInvalidCredentialException("User not found")
                    );

            if (Boolean.FALSE.equals(user.getIsActive())) {
                throw new UserInvalidCredentialException("User is not active");
            }

            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            String token = jwtUtil.generateToken(user);

            return new LoginResponseDTO(
                    token,
                    user.getUsername(),
                    user.getRole().getName()
            );

        } catch (AuthenticationException e) {
            throw new UserInvalidCredentialException("Invalid username or password");
        }
    }

    @Override
    public Boolean verifyByPassword(LoginRequestDTO data) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new UserInvalidCredentialException("Unauthorized");
        }

        String username = auth.getName();

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserInvalidCredentialException("User not found"));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new UserInvalidCredentialException("User is not active");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, data.password())
            );
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }
}
