package com.youcode.covoiturageapi.service;

import com.youcode.covoiturageapi.dto.TrajetRequestDto;
import com.youcode.covoiturageapi.dto.TrajetResponseDto;

import java.util.List;

public interface TrajetService {

    TrajetResponseDto create(TrajetRequestDto requestDto);
    TrajetResponseDto findById(Long id);
    List<TrajetResponseDto> findAll();
    TrajetResponseDto terminer(Long id);
    TrajetResponseDto annuler(Long id);
}
