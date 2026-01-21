package com.sb.file.compressor.model.student.repo;

import com.sb.file.compressor.model.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 9:25 PM
 * -------------------------------------------------------------
 */
public interface StudentRepository extends JpaRepository<Student,Long> {
}
