package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import com.pulsecare.backend.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponseDTO findById(String id) {
        return null;
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return Collections.emptyList();
    }

    @Override
    public UserResponseDTO create(UserRequestDTO data) {
        return null;
    }

    @Override
    public UserResponseDTO update(UserRequestDTO data) {
        return null;
    }

    @Override
    public Byte delete(String id) {
        return null;
    }

    @Override
    public String login(LoginRequestDTO data) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            data.username(),
                            data.password()
                    )
            );

            if (authentication.isAuthenticated()) {
                Users user = userRepository.findByUsername(data.username());
                return jwtUtil.generateToken(user);
            }

            return "Login failed";

        } catch (AuthenticationException e) {
            return "Invalid username or password";
        }
    }
}
