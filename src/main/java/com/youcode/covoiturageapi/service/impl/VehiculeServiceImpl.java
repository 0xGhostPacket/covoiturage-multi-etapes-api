package com.youcode.covoiturageapi.service.impl;

import com.youcode.covoiturageapi.dto.VehiculeRequestDto;
import com.youcode.covoiturageapi.dto.VehiculeResponseDto;
import com.youcode.covoiturageapi.entity.Conducteur;
import com.youcode.covoiturageapi.entity.Vehicule;
import com.youcode.covoiturageapi.exception.BusinessRuleException;
import com.youcode.covoiturageapi.exception.ResourceNotFoundException;
import com.youcode.covoiturageapi.mapper.VehiculeMapper;
import com.youcode.covoiturageapi.repository.ConducteurRepository;
import com.youcode.covoiturageapi.repository.VehiculeRepository;
import com.youcode.covoiturageapi.service.VehiculeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;
    private final ConducteurRepository conducteurRepository;

    @Override
    public VehiculeResponseDto create(VehiculeRequestDto requestDto) {

        // Rule 1: Check driver exists
        Conducteur conducteur = conducteurRepository.findById(requestDto.conducteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur introuvable avec l'id : " + requestDto.conducteurId()));

        // Rule 2: Check license plate is unique
        if (vehiculeRepository.existsByImmatriculation(requestDto.immatriculation())) {
            throw new BusinessRuleException("Un véhicule avec cette immatriculation existe déjà");
        }
        //Rule 3: Map, attach driver, and save
        Vehicule vehicule = vehiculeMapper.toEntity(requestDto);
        vehicule.setConducteur(conducteur); // Manually attach the managed entity!
        Vehicule saved = vehiculeRepository.save(vehicule);
        log.info("Véhicule créé avec succès : ID={}, Conducteur={}", saved.getId(), conducteur.getId());
        return vehiculeMapper.toResponseDto(saved);
    }

    @Override
    public VehiculeResponseDto findById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id : " + id));
        return vehiculeMapper.toResponseDto(vehicule);
    }

    @Override
    public List<VehiculeResponseDto> findByConducteurId(Long conducteurId) {
        List<Vehicule> vehiculeList = vehiculeRepository.findByConducteurId(conducteurId);
        return vehiculeMapper.toResponseDtoList(vehiculeList);
    }

    @Override
    public VehiculeResponseDto update(Long id, VehiculeRequestDto requestDto) {
        // 1. Check if vehicle exists
        Vehicule existing = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id : " + id));

        // 2. Check if the new plate is already used by another vehicle
        if (vehiculeRepository.existsByImmatriculationAndIdNot(requestDto.immatriculation(), id)) {
            throw new BusinessRuleException("Un autre véhicule possède déjà cette immatriculation");
        }

        // 3. Update the fields
        existing.setMarque(requestDto.marque());
        existing.setModele(requestDto.modele());
        existing.setImmatriculation(requestDto.immatriculation());
        existing.setCouleur(requestDto.couleur());
        existing.setCapacite(requestDto.capacite());

        // 4. Save, log, and return response DTO
        Vehicule updated = vehiculeRepository.save(existing);
        log.info("Véhicule mis à jour avec succès : ID={}", updated.getId());

        return vehiculeMapper.toResponseDto(updated);
    }

    @Override
    public void delete(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id : " + id));

        vehiculeRepository.delete(vehicule);
        log.info("Véhicule supprimé avec succès : ID={}", id);
    }
}
