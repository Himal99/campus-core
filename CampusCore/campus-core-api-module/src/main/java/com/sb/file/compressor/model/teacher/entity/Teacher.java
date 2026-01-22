package com.sb.file.compressor.model.teacher.entity;

import com.sb.file.compressor.core.entity.BaseEntity;
import com.sb.file.compressor.core.enums.Gender;
import com.sb.file.compressor.model.teacher.enums.*;
import com.sb.file.compressor.model.teacher.enums.ClassEnum;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity<Long> {


    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(unique = true, nullable = false, length = 15)
    private String phone;

    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profileImageUrl;

    // ================= ACADEMIC INFO =================
    @Column(nullable = false)
    private String employeeCode;   // Unique college employee ID

    @Enumerated(EnumType.STRING)
    private Designation designation;    // Lecturer, Assistant Professor, HOD

    @Enumerated(EnumType.STRING)
    private Qualification qualification;  // MSc, PhD, MTech

    private String specialization; // AI, Networks, Accounting

    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    private Department department;
    // Computer Science, Management
    @Enumerated(EnumType.STRING)
    private Faculty facultyType;    // Full-time, Part-time, Visiting

    private Boolean isHod;

    // ================= TEACHING INFO =================
    private Subject subjectsHandled; // "Java, DBMS, AI" (can normalize later)

    private ClassEnum classesHandled;  // "BCA 3rd Sem, BSc CS"

    // ================= EMPLOYMENT INFO =================
    private Date joiningDate;

    private Double salary;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus status; // ACTIVE, INACTIVE, RESIGNED

    // ================= ADDRESS =================
    private String address;
    private String city;
    private String province;
    private String country;

    // ================= SYSTEM / AUDIT =================
    private Boolean active;


}
