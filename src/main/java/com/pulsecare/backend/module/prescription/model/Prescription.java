package com.pulsecare.backend.module.prescription.model;

import com.pulsecare.backend.module.patient_admission.model.PatientAdmission;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.prescription.enums.PrescriptionStatus;
import com.pulsecare.backend.module.prescription.enums.PrescriptionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "queue_id")
    private PatientQueue patientQueue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id")
    private PatientAdmission admission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionType type;

    @Column(length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, insertable = false)
    private LocalDateTime updatedAt;

}

