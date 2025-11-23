package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.user.dto.LoginRequestDTO;
import com.pulsecare.backend.module.user.exception.UserInvalidCredentialException;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import com.pulsecare.backend.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository repository, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Users findById(String id) {
        Users data = repository.findById(UUID.fromString(id)).orElse(null);
        if (data == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return data;
    }

    @Override
    public List<Users> findAll() {
        return repository.findAll();
    }

    @Override
    public Users create(Users data) {
        if (repository.findByUsername(data.getUsername()) == null)
            throw new ResourceAlreadyExistsException("User with this username already exists");

        return repository.save(data);
    }

    @Override
    public Users update(String id, Users data) {
        repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        return repository.save(data);
    }

    @Override
    public void delete(String id) {
        Users entity = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        repository.delete(entity);
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

            Users user = repository.findByUsername(data.username());
            if (user == null) {
                throw new UserInvalidCredentialException("User not found");
            }

            user.setLastLoginAt(LocalDateTime.now());
            repository.save(user);

            return jwtUtil.generateToken(user);

        } catch (AuthenticationException e) {
            throw new UserInvalidCredentialException("Invalid username or password");
        }
    }


}
