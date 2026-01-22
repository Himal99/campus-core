package com.sb.file.compressor.web.teacherV1;

import com.sb.file.compressor.model.teacher.entity.Teacher;
import com.sb.file.compressor.model.teacher.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 10:30 PM
 * -------------------------------------------------------------
 */

@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<Teacher> saveTeacher(@RequestBody Teacher teacher) {
        Teacher save = teacherService.save(teacher);
        return ResponseEntity.ok(save);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable Long id) {
        Teacher one = teacherService.findOne(id);
        return ResponseEntity.ok(one);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Teacher>> getTeacherList() {
        List<Teacher> list = teacherService.findAll();
        return ResponseEntity.ok(list);
    }
}
