package com.youcode.covoiturageapi.service.impl;

import com.youcode.covoiturageapi.dto.ConducteurRequestDto;
import com.youcode.covoiturageapi.dto.ConducteurResponseDto;
import com.youcode.covoiturageapi.entity.Conducteur;
import com.youcode.covoiturageapi.exception.BusinessRuleException;
import com.youcode.covoiturageapi.exception.ResourceNotFoundException;
import com.youcode.covoiturageapi.mapper.ConducteurMapper;
import com.youcode.covoiturageapi.repository.ConducteurRepository;
import com.youcode.covoiturageapi.service.ConducteurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.spi.ServiceRegistry;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConducteurServiceImpl implements ConducteurService {

    // DI
    private final ConducteurRepository conducteurRepository;
    private final ConducteurMapper conducteurMapper;

    // create
    @Override
    public ConducteurResponseDto create(ConducteurRequestDto requestDto) {
        if (conducteurRepository.existsByEmail(requestDto.email())) {
            throw new BusinessRuleException("L'email est déjà utilisé par un autre conducteu");
        }
        if (conducteurRepository.existsByNumeroPermis(requestDto.numeroPermis())) {
            throw new BusinessRuleException("Le numéro de permis est déjà utilisé");
        }
        // convert dto to entity
        Conducteur conducteur = conducteurMapper.toEntity(requestDto);
        // save
        Conducteur save = conducteurRepository.save(conducteur);
        log.info("Conducteur créé avec succès : ID={}", save.getId());
        return conducteurMapper.toResponseDto(save);
    }

    // find by id
    @Override
    public ConducteurResponseDto findById(Long id) {
        Conducteur conducteur = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur introuvable avec l'id : " + id));
        return conducteurMapper.toResponseDto(conducteur);
    }

    // find all


    @Override
    public List<ConducteurResponseDto> findAll() {
        List<Conducteur> conducteur = conducteurRepository.findAll();
        return conducteurMapper.toResponseDtoList(conducteur);

    }

    // update
    @Override
    public ConducteurResponseDto update(Long id, ConducteurRequestDto requestDto) {

        Conducteur existing = conducteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur introuvable avec l'id : " + id));

        if (conducteurRepository.existsByEmailAndIdNot(requestDto.email(), id)) {
            throw new BusinessRuleException("L'email est déjà utilisé par un autre conducteur");
        }

        if (conducteurRepository.existsByNumeroPermisAndIdNot(requestDto.numeroPermis(),id)) {
            throw new BusinessRuleException("Le numéro de permis est déjà utilisé");
        }

        existing.setNom(requestDto.nom());
        existing.setPrenom(requestDto.prenom());
        existing.setEmail(requestDto.email());
        existing.setTelephone(requestDto.telephone());
        existing.setNumeroPermis(requestDto.numeroPermis());

        Conducteur updated = conducteurRepository.save(existing);
        log.info("Conducteur mis à jour avec succès : ID={}", updated.getId());
        return conducteurMapper.toResponseDto(updated);
    }

    // search
    @Override
    public List<ConducteurResponseDto> search(String query) {
        List<Conducteur> results = conducteurRepository.findByNomContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
        return conducteurMapper.toResponseDtoList(results);
    }

}
