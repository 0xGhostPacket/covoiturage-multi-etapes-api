package com.youcode.covoiturageapi.controller;

import com.youcode.covoiturageapi.dto.ConducteurRequestDto;
import com.youcode.covoiturageapi.dto.ConducteurResponseDto;
import com.youcode.covoiturageapi.service.ConducteurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conducteurs")
@RequiredArgsConstructor

public class ConducteurController {
    //DI
    private final ConducteurService conducteurService;

    // create end point
    @PostMapping
    public ResponseEntity<ConducteurResponseDto> create(@Valid @RequestBody ConducteurRequestDto requestDto)
    {
        ConducteurResponseDto conducteurResponseDto = conducteurService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(conducteurResponseDto);

    }

    // find by id endpoint
    @GetMapping("/{id}")
    public ResponseEntity<ConducteurResponseDto> findById(@PathVariable Long id) {
        ConducteurResponseDto conducteurResponseDto = conducteurService.findById(id);
        return ResponseEntity.ok(conducteurResponseDto);
    }

    // find all
    @GetMapping
    public ResponseEntity<List<ConducteurResponseDto>> getAll(@RequestParam(required = false) String search)
    {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(conducteurService.search(search));
        }
        return ResponseEntity.ok(conducteurService.findAll());
    }

    // update by id
    @PutMapping("/{id}")
    public ResponseEntity<ConducteurResponseDto> update(@PathVariable Long id, @Valid @RequestBody ConducteurRequestDto requestDto) {
        ConducteurResponseDto updated = conducteurService.update(id, requestDto);
        return ResponseEntity.ok(updated);
    }
}
