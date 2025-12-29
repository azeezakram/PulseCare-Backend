package com.pulsecare.backend.module.user.repository;

import com.pulsecare.backend.module.user.dto.UserImageProjection;
import com.pulsecare.backend.module.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);
    @Query("""
        SELECT u.imageData AS imageData,
               u.contentType AS contentType
        FROM Users u
        WHERE u.id = :id
    """)
    Optional<UserImageProjection> findUserImageById(UUID id);
}
