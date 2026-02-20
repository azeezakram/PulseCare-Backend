package com.pulsecare.backend.module.user.repository;

import com.pulsecare.backend.module.user.dto.UserImageProjection;
import com.pulsecare.backend.module.user.dto.UserLoginView;
import com.pulsecare.backend.module.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);

    @Query("""
    SELECT new com.pulsecare.backend.module.user.dto.UserLoginView(
        u.id, u.username, u.password, 
        u.firstName, u.lastName, 
        u.isActive, r
    )
    FROM Users u 
    JOIN u.role r
    WHERE u.username = :username
""")
    Optional<UserLoginView> findLoginUser(String username);


    @Query("""
                SELECT u.imageData AS imageData,
                       u.contentType AS contentType
                FROM Users u
                WHERE u.id = :id
            """)
    Optional<UserImageProjection> findUserImageById(UUID id);

    @Modifying
    @Transactional
    @Query("""
                UPDATE Users u 
                SET u.imageData = :imageData,
                    u.imageName = :imageName,
                    u.contentType = :contentType,
                    u.updatedAt = CURRENT_TIMESTAMP
                WHERE u.id = :userId
            """)
    void updateProfileImage(
            @Param("userId") UUID userId,
            @Param("imageData") byte[] imageData,
            @Param("imageName") String imageName,
            @Param("contentType") String contentType
    );
}
