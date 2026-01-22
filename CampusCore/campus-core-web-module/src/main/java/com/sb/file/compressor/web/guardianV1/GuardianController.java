package com.sb.file.compressor.web.guardianV1;

import com.sb.file.compressor.model.gurdian.entity.Guardian;
import com.sb.file.compressor.model.gurdian.service.GuardianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * -------------------------------------------------------------
 * |   Author      : Himal Rai
 * |   Department  : JAVA
 * |   Company     : DIGI Hub
 * |   Created     : 1/21/2026 10:15 PM
 * -------------------------------------------------------------
 */
@RestController
@RequestMapping("/api/v1/guardian")
public class GuardianController {
    private final GuardianService guardianService;

    public GuardianController(GuardianService guardianService) {
        this.guardianService = guardianService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Guardian guardian) {
        Guardian save = this.guardianService.save(guardian);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Guardian one = this.guardianService.findOne(id);
        return new ResponseEntity<>(one, HttpStatus.OK);
    }

    @GetMapping("/list")

    public ResponseEntity<?> findAll() {
        List<Guardian> list = this.guardianService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
