package com.youcode.covoiturageapi.controller;

import com.youcode.covoiturageapi.dto.PassagerRequestDto;
import com.youcode.covoiturageapi.dto.PassagerResponseDto;
import com.youcode.covoiturageapi.service.PassagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passagers")
@RequiredArgsConstructor
public class PassagerController {

    private final PassagerService passagerService;

    // 1. Create Passenger (POST /api/v1/passagers -> 201 Created)
    @PostMapping
    public ResponseEntity<PassagerResponseDto> create(@Valid @RequestBody PassagerRequestDto requestDto) {
        PassagerResponseDto response = passagerService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Find by ID (GET /api/v1/passagers/{id} -> 200 OK)
    @GetMapping("/{id}")
    public ResponseEntity<PassagerResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(passagerService.findById(id));
    }

    // 3. Find All or Search by Email (GET /api/v1/passagers?email=... -> 200 OK)
    @GetMapping
    public ResponseEntity<List<PassagerResponseDto>> getAll(@RequestParam(required = false) String email) {
        if (email != null && !email.isBlank()) {
            return ResponseEntity.ok(List.of(passagerService.findByEmail(email)));
        }
        return ResponseEntity.ok(passagerService.findAll());
    }

    // 4. Update Passenger (PUT /api/v1/passagers/{id} -> 200 OK)
    @PutMapping("/{id}")
    public ResponseEntity<PassagerResponseDto> update(@PathVariable Long id, @Valid @RequestBody PassagerRequestDto requestDto) {
        return ResponseEntity.ok(passagerService.update(id, requestDto));
    }
}