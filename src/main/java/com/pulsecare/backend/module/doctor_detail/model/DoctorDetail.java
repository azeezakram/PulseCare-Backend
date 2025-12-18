package com.pulsecare.backend.module.doctor_detail.model;

import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.user.model.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
@Entity
@Table(name = "doctor_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DoctorDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String licenseNo;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "doctor_specializations",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    @ToString.Exclude
    private Set<Specialization> specializations;
}
