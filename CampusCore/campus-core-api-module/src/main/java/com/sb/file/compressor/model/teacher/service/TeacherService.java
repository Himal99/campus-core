package com.sb.file.compressor.model.teacher.service;

import com.sb.file.compressor.core.service.BaseService;
import com.sb.file.compressor.model.teacher.entity.Teacher;
import com.sb.file.compressor.model.teacher.repo.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 10:29 PM
 * -------------------------------------------------------------
 */

@Service
public class TeacherService implements BaseService<Teacher> {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher findOne(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    @Override
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Page<Teacher> findAllPageable(Object t, Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }

    @Override
    public List<Teacher> saveAll(List<Teacher> list) {
        return teacherRepository.saveAll(list);
    }
}
