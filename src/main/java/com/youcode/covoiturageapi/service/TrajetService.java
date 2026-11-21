package com.youcode.covoiturageapi.service;

import com.youcode.covoiturageapi.dto.TrajetRequestDto;
import com.youcode.covoiturageapi.dto.TrajetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TrajetService {

    TrajetResponseDto create(TrajetRequestDto requestDto);
    TrajetResponseDto findById(Long id);
    List<TrajetResponseDto> findAll();
    TrajetResponseDto terminer(Long id);
    TrajetResponseDto annuler(Long id);
    Page<TrajetResponseDto> search(String villeDepart, String villeArrivee, LocalDate date, Pageable pageable);
}
