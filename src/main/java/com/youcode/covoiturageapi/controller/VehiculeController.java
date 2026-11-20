package com.youcode.covoiturageapi.controller;

import com.youcode.covoiturageapi.dto.VehiculeRequestDto;
import com.youcode.covoiturageapi.dto.VehiculeResponseDto;
import com.youcode.covoiturageapi.service.VehiculeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @PostMapping
    public ResponseEntity<VehiculeResponseDto> create(@Valid @RequestBody VehiculeRequestDto requestDto) {
        VehiculeResponseDto response = vehiculeService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @GetMapping("/conducteur/{conducteurId}")
    public ResponseEntity<List<VehiculeResponseDto>> findByConducteurId(@PathVariable Long conducteurId) {
        return ResponseEntity.ok(vehiculeService.findByConducteurId(conducteurId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<VehiculeResponseDto> update(@PathVariable Long id, @Valid @RequestBody VehiculeRequestDto requestDto) {
        return ResponseEntity.ok(vehiculeService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehiculeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}