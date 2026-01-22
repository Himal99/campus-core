package com.sb.file.compressor.model.student.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sb.file.compressor.core.entity.BaseEntity;
import com.sb.file.compressor.core.enums.Gender;
import com.sb.file.compressor.model.gurdian.entity.Guardian;
import com.sb.file.compressor.model.student.enums.AdmissionType;
import com.sb.file.compressor.model.student.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(
        name = "students",
        indexes = {
                @Index(name = "idx_student_email", columnList = "email"),
                @Index(name = "idx_student_roll", columnList = "rollNo"),
                @Index(name = "idx_student_reg", columnList = "registrationNo")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity<Long> {

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profileImageUrl;

    // ================= ACADEMIC INFO =================
    @Column(nullable = false, unique = true)
    private String registrationNo;   // University registration number

    @Column(nullable = false, unique = true)
    private String rollNo;           // College roll number

    private String faculty;          // BCA, BSc CS, BBA
    private String department;       // Computer Science

    private Integer semester;
    private Integer batchYear;       // 2022, 2023

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date admissionDate;

    @Enumerated(EnumType.STRING)
    private AdmissionType admissionType; // REGULAR, LATERAL, SCHOLARSHIP

    // ================= ADDRESS =================
    private String address;
    private String city;
    private String province;
    private String country;

    // ================= SYSTEM / STATUS =================
    @Enumerated(EnumType.STRING)
    private StudentStatus status; // ACTIVE, SUSPENDED, PASSED_OUT, DROPPED

    private Boolean active;


    @OneToOne
    private Guardian guardian;

}
