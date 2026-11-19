package com.youcode.covoiturageapi.service;

import com.youcode.covoiturageapi.dto.PassagerRequestDto;
import com.youcode.covoiturageapi.dto.PassagerResponseDto;

import java.util.List;

public interface PassagerService {

    PassagerResponseDto create(PassagerRequestDto requestDto);
    PassagerResponseDto findById(Long id);
    List<PassagerResponseDto> findAll();
    PassagerResponseDto findByEmail(String email);
    PassagerResponseDto update(Long id, PassagerRequestDto requestDto);

}
