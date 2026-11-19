package com.youcode.covoiturageapi.service.impl;

import com.youcode.covoiturageapi.dto.PassagerRequestDto;
import com.youcode.covoiturageapi.dto.PassagerResponseDto;
import com.youcode.covoiturageapi.entity.Passager;
import com.youcode.covoiturageapi.exception.BusinessRuleException;
import com.youcode.covoiturageapi.exception.ResourceNotFoundException;
import com.youcode.covoiturageapi.mapper.PassagerMapper;
import com.youcode.covoiturageapi.repository.PassagerRepository;
import com.youcode.covoiturageapi.service.PassagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassagerServiceImpl implements PassagerService {

    private final PassagerRepository passagerRepository;
    private final PassagerMapper passagerMapper;

    @Override
    public PassagerResponseDto create(PassagerRequestDto requestDto) {
        if (passagerRepository.existsByEmail(requestDto.email())) {
            throw new BusinessRuleException("L'email est déjà utilisé par un autre passager");
        }

        Passager passager = passagerMapper.toEntity(requestDto);
        Passager saved = passagerRepository.save(passager);
        log.info("Passager créé avec succès : ID={}", saved.getId());

        return passagerMapper.toResponseDto(saved);
    }

    @Override
    public PassagerResponseDto findById(Long id) {
        Passager passager = passagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passager introuvable avec l'id : " + id));
        return passagerMapper.toResponseDto(passager);
    }

    @Override
    public PassagerResponseDto findByEmail(String email) {
        Passager passager = passagerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Passager introuvable avec l'email : " + email));
        return passagerMapper.toResponseDto(passager);
    }

    @Override
    public List<PassagerResponseDto> findAll() {
        List<Passager> passagers = passagerRepository.findAll();
        return passagerMapper.toResponseDtoList(passagers);
    }

    @Override
    public PassagerResponseDto update(Long id, PassagerRequestDto requestDto) {
        Passager existing = passagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passager introuvable avec l'id : " + id));

        if (passagerRepository.existsByEmailAndIdNot(requestDto.email(), id)) {
            throw new BusinessRuleException("L'email est déjà utilisé par un autre passager");
        }

        existing.setNom(requestDto.nom());
        existing.setPrenom(requestDto.prenom());
        existing.setEmail(requestDto.email());
        existing.setTelephone(requestDto.telephone());

        Passager updated = passagerRepository.save(existing);
        log.info("Passager mis à jour avec succès : ID={}", updated.getId());

        return passagerMapper.toResponseDto(updated);
    }
}