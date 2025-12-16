package com.pulsecare.backend.module.triage.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "triage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Triage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // ========== Basic Categorical Features ==========
    @Column(nullable = false)
    private Integer sex; // 0=Female, 1=Male

    @Column(name = "arrival_mode", nullable = false)
    private Integer arrivalMode; // 1=Walking, 2=Public Ambulance, 3=Private Vehicle, 4=Private Ambulance, 5/6/7=Other

    @Column(nullable = false)
    private Integer injury; // 1=Yes, 2=No

    @Column(nullable = false)
    private Integer mental; // 1=Alert, 2=Verbal Response, 3=Pain Response, 4=Unresponsive

    @Column(nullable = false)
    private Integer pain; // 1=Yes, 0=No

    // ========== Basic Numerical Features ==========
    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Integer sbp; // Systolic Blood Pressure

    @Column(nullable = false)
    private Integer dbp; // Diastolic Blood Pressure

    @Column(nullable = false)
    private Integer hr; // Heart Rate

    @Column(nullable = false)
    private Integer rr; // Respiratory Rate

    @Column(nullable = false)
    private Double bt; // Body Temperature

    // ========== Derived Numerical Features (Nullable) ==========
    @Column(name = "shock_index")
    private Double shockIndex;

    @Column(name = "pulse_pressure")
    private Double pulsePressure;

    @Column(name = "pp_ratio")
    private Double ppRatio;

    @Column(name = "hr_bt_interaction")
    private Double hrBtInteraction;

    @Column(name = "rr_hr_ratio")
    private Double rrHrRatio;

    // ========== Derived Categorical Features (Nullable) ==========
    @Column(name = "is_fever")
    private Boolean isFever;

    @Column(name = "is_tachy")
    private Boolean isTachy;

    @Column(name = "is_low_sbp")
    private Boolean isLowSbp;

    @Column(name = "is_low_dbp")
    private Boolean isLowDbp;

    @Column(name = "is_tachypnea")
    private Boolean isTachypnea;

    // ========== Target Label (0 = Critical, 1 = Non-Critical) ==========
    @Column(name = "triage_level", nullable = false)
    private Integer triageLevel;

    // ========== Audit Fields ==========
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}