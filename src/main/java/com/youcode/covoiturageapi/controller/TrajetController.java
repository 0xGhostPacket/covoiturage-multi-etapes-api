package com.youcode.covoiturageapi.controller;

import com.youcode.covoiturageapi.dto.TrajetRequestDto;
import com.youcode.covoiturageapi.dto.TrajetResponseDto;
import com.youcode.covoiturageapi.service.TrajetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trajets")
@RequiredArgsConstructor
public class TrajetController {

    private final TrajetService trajetService;

    // 1. Publish a trip (POST /api/v1/trajets -> 201 Created)
    @PostMapping
    public ResponseEntity<TrajetResponseDto> create(@Valid @RequestBody TrajetRequestDto requestDto) {
        TrajetResponseDto response = trajetService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Get trip details by ID without N+1 (GET /api/v1/trajets/{id} -> 200 OK)
    @GetMapping("/{id}")
    public ResponseEntity<TrajetResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(trajetService.findById(id));
    }

    // 3. Get all trips (GET /api/v1/trajets -> 200 OK)
    @GetMapping
    public ResponseEntity<List<TrajetResponseDto>> findAll() {
        return ResponseEntity.ok(trajetService.findAll());
    }

    // 4. Terminate trip - SCENARIO 9 (PATCH /api/v1/trajets/{id}/terminer -> 200 OK)
    @PatchMapping("/{id}/terminer")
    public ResponseEntity<TrajetResponseDto> terminer(@PathVariable Long id) {
        return ResponseEntity.ok(trajetService.terminer(id));
    }

    // 5. Cancel trip - SCENARIO 11 (PATCH /api/v1/trajets/{id}/annuler -> 200 OK)
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<TrajetResponseDto> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(trajetService.annuler(id));
    }

    // 6. Search trips by departure city, arrival city, and date (Scenario 2)
    @GetMapping("/search")
    public ResponseEntity<Page<TrajetResponseDto>> search(
            @RequestParam String depart,
            @RequestParam String arrivee,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 10, sort = "dateDepart") Pageable pageable) {

        Page<TrajetResponseDto> results = trajetService.search(depart, arrivee, date, pageable);
        return ResponseEntity.ok(results);
    }
}