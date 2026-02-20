package com.pulsecare.backend.module.patient_admission.model;

import com.pulsecare.backend.module.patient.model.Patient;
import com.pulsecare.backend.module.patient_admission.enums.PatientAdmissionStatus;
import com.pulsecare.backend.module.patient_queue.model.PatientQueue;
import com.pulsecare.backend.module.resource.bed.model.Bed;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "patient_admission",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "bed_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAdmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "queue_id")
    private PatientQueue patientQueue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id")
    private Bed bed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientAdmissionStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime admittedAt;

    private LocalDateTime dischargedAt;

    private String dischargeNotes;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
