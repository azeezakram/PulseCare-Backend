package com.pulsecare.backend.module.specialization.model;

import com.pulsecare.backend.module.doctor_detail.model.DoctorDetail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "specialization")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "specializations")
    @ToString.Exclude
    private Set<DoctorDetail> doctors;
}

