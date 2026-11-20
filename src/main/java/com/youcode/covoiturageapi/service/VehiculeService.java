package com.youcode.covoiturageapi.service;

import com.youcode.covoiturageapi.dto.VehiculeRequestDto;
import com.youcode.covoiturageapi.dto.VehiculeResponseDto;

import java.util.List;

public interface VehiculeService {

    VehiculeResponseDto create (VehiculeRequestDto requestDto);
    VehiculeResponseDto findById(Long id);
    List<VehiculeResponseDto> findByConducteurId(Long conducteurId);
    VehiculeResponseDto update(Long id, VehiculeRequestDto requestDto);
    void delete(Long id);
}
