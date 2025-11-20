package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.exception.UserInvalidCredentialException;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import com.pulsecare.backend.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public void delete(String id) {
        // TODO document why this method is empty
    }

    @Override
    public String login(LoginRequestDTO data) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            data.username(),
                            data.password()
                    )
            );

            Users user = userRepository.findByUsername(data.username());
            if (user == null) {
                throw new UserInvalidCredentialException("User not found");
            }

            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            return jwtUtil.generateToken(user);

        } catch (AuthenticationException e) {
            throw new UserInvalidCredentialException("Invalid username or password");
        }
    }


    @Override
    public UserResponseDTO update(String s, UserRequestDTO data) {
        return null;
    }
}
