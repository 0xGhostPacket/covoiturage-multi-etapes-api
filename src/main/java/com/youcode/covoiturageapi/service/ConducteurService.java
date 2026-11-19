package com.youcode.covoiturageapi.service;

import com.youcode.covoiturageapi.dto.ConducteurRequestDto;
import com.youcode.covoiturageapi.dto.ConducteurResponseDto;
import com.youcode.covoiturageapi.entity.Conducteur;

import java.util.List;

public interface ConducteurService {
    // create
    ConducteurResponseDto create(ConducteurRequestDto requestDto);
    // update
    ConducteurResponseDto update(Long id , ConducteurRequestDto requestDto);
    // find by id
    ConducteurResponseDto findById(Long id);
    // find all
    List<ConducteurResponseDto> findAll();
    // Search
    List<ConducteurResponseDto> search(String query);

}