package com.sb.file.compressor.model.student.dto;

import com.sb.file.compressor.core.enums.Gender;
import com.sb.file.compressor.model.student.enums.AdmissionType;
import com.sb.file.compressor.model.student.enums.StudentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 9:28 PM
 * -------------------------------------------------------------
 */

@Getter
@Setter
public class StudentRequestDto {

    private Long id;

    private String firstName;

  
    private String lastName;


    private String email;


    private String phone;

    private Date dateOfBirth;


    private Gender gender;

    private String profileImageUrl;


    private String registrationNo;   // University registration number


    private String rollNo;           // College roll number

    private String faculty;          // BCA, BSc CS, BBA
    private String department;       // Computer Science

    private Integer semester;
    private Integer batchYear;       // 2022, 2023

    // ================= ADMISSION INFO =================
    private Date admissionDate;


    private AdmissionType admissionType; // REGULAR, LATERAL, SCHOLARSHIP

    // ================= GUARDIAN INFO =================
    private String guardianName;
    private String guardianPhone;
    private String guardianRelation;

    // ================= ADDRESS =================
    private String address;
    private String city;
    private String province;
    private String country;


    private StudentStatus status; // ACTIVE, SUSPENDED, PASSED_OUT, DROPPED

    private Boolean active;


    @Override
    public String toString() {
        return "StudentRequestDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", registrationNo='" + registrationNo + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", faculty='" + faculty + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                ", batchYear=" + batchYear +
                ", admissionDate=" + admissionDate +
                ", admissionType=" + admissionType +
                ", guardianName='" + guardianName + '\'' +
                ", guardianPhone='" + guardianPhone + '\'' +
                ", guardianRelation='" + guardianRelation + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", status=" + status +
                ", active=" + active +
                '}';
    }
}
