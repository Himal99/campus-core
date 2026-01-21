package com.sb.file.compressor.web.studentV1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.model.student.dto.StudentRequestDto;
import com.sb.file.compressor.model.student.entity.Student;
import com.sb.file.compressor.model.student.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 9:30 PM
 * -------------------------------------------------------------
 */
@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;
    private final ObjectMapper objectMapper;

    public StudentController(StudentService studentService, ObjectMapper objectMapper) {
        this.studentService = studentService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StudentRequestDto student) {
        Student student1 = this.objectMapper.convertValue(student, Student.class);
        Student save = studentService.save(student1);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Student one = studentService.findOne(id);
        return new ResponseEntity<>(one, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAll() {
        List<Student> all = studentService.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


}
