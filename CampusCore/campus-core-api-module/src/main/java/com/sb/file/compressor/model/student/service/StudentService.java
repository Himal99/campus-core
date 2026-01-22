package com.sb.file.compressor.model.student.service;

import com.sb.file.compressor.core.service.BaseService;
import com.sb.file.compressor.model.student.entity.Student;
import com.sb.file.compressor.model.student.repo.StudentRepository;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 9:26 PM
 * -------------------------------------------------------------
 */

@Service
public class StudentService implements BaseService<Student> {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findOne(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student save(Student student) {

        return studentRepository.save(student);
    }

    @Override
    public Page<Student> findAllPageable(Object t, Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<Student> saveAll(List<Student> list) {
        return studentRepository.saveAll(list);
    }
}
