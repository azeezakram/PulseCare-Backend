package com.pulsecare.backend.module.user.model;

import com.pulsecare.backend.module.role.model.Role;
import com.pulsecare.backend.module.doctordetail.model.DoctorDetail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 25)
    private String firstName;
    @Column(nullable = false, length = 25)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = true, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = true)
    private String mobileNumber;

    //Profile picture attributes
    @Column(nullable = true)
    private String imageName;
    @Column(nullable = true)
    private String contentType;
    @Column(nullable = true)
    @Lob
    private byte[] imageData;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(nullable = false, insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(nullable = false, insertable = false)
    private LocalDateTime lastLoginAt;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private DoctorDetail doctorDetails;

}
