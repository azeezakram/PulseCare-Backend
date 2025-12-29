package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.user.dto.UserImageProjection;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Users findById(String id) {
        return repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public List<Users> findAll() {
        return repository.findAll();
    }

    @Override
    public Users save(Users user) {
        return repository.save(user);
    }

    @Override
    public void delete(String id) {
        Users entity = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        repository.delete(entity);
    }

    public UserImageProjection getUserProfileImage(UUID userId) {
        return repository.findUserImageById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile image not found"));
    }

    @Override
    public void validateUsernameUniqueness(String newUsername, UUID currentUserId) {
        Users existByUsername = repository.findByUsername(newUsername);
        if (existByUsername != null && !existByUsername.getId().equals(currentUserId)) {
            throw new ResourceAlreadyExistsException("User with this username already exists");
        }
    }

    @Override
    public void validateUsernameDoesNotExist(String username) {
        Users existingUser = repository.findByUsername(username);
        if (existingUser != null) {
            throw new ResourceAlreadyExistsException("User with username '" + username + "' already exists");
        }
    }


}
