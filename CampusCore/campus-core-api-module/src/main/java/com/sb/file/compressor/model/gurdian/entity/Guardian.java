package com.sb.file.compressor.model.gurdian.entity;

import com.sb.file.compressor.model.gurdian.enums.GuardianRelation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "guardians",
    indexes = {
        @Index(name = "idx_guardian_phone", columnList = "phone"),
        @Index(name = "idx_guardian_email", columnList = "email")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guardian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= BASIC INFO =================
    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 150)
    private String email;

    @Enumerated(EnumType.STRING)
    private GuardianRelation relation; // FATHER, MOTHER, GUARDIAN

    private String occupation;

    // ================= ADDRESS =================
    private String address;
    private String city;
    private String province;
    private String country;

    // ================= SYSTEM / AUDIT =================
    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
