package com.youcode.covoiturageapi.service.impl;

import com.youcode.covoiturageapi.dto.PointArretRequestDto;
import com.youcode.covoiturageapi.dto.TrajetRequestDto;
import com.youcode.covoiturageapi.dto.TrajetResponseDto;
import com.youcode.covoiturageapi.entity.Conducteur;
import com.youcode.covoiturageapi.entity.Trajet;
import com.youcode.covoiturageapi.entity.Vehicule;
import com.youcode.covoiturageapi.enums.StatutTrajet;
import com.youcode.covoiturageapi.exception.BusinessRuleException;
import com.youcode.covoiturageapi.exception.ForbiddenActionException;
import com.youcode.covoiturageapi.exception.ResourceNotFoundException;
import com.youcode.covoiturageapi.mapper.PointArretMapper;
import com.youcode.covoiturageapi.mapper.TrajetMapper;
import com.youcode.covoiturageapi.repository.ConducteurRepository;
import com.youcode.covoiturageapi.repository.TrajetRepository;
import com.youcode.covoiturageapi.repository.VehiculeRepository;
import com.youcode.covoiturageapi.service.TrajetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final ConducteurRepository conducteurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetMapper trajetMapper;
    private final PointArretMapper pointArretMapper;

    // ─────────────────────────────────────────────────────────────
    // VALIDATIONS (Private Helper Methods)
    // ─────────────────────────────────────────────────────────────

    // SCENARIO 8: Vehicle ownership check (403 Forbidden)
    private void validateOwnership(Vehicule vehicule, Long conducteurId) {
        if (!vehicule.getConducteur().getId().equals(conducteurId)) {
            log.warn("Véhicule #{} n'appartient pas au conducteur #{}", vehicule.getId(), conducteurId);
            throw new ForbiddenActionException("Ce véhicule n'appartient pas à ce conducteur");
        }
    }

    // SCENARIO 7: Capacity check (400 Bad Request)
    private void validateCapacity(Integer placesProposees, Integer capaciteVehicule) {
        if (placesProposees > capaciteVehicule) {
            throw new BusinessRuleException(
                    "Le nombre de places (" + placesProposees + ") dépasse la capacité du véhicule (" + capaciteVehicule + ")"
            );
        }
    }

    // Route Rules validation
    private void validateStopPoints(List<PointArretRequestDto> sorted) {
        // Rule 1: At least 2 stops
        if (sorted.size() < 2) {
            throw new BusinessRuleException("Le trajet doit comporter au moins 2 points d'arrêt");
        }

        // Rule 2: First stop price must be 0.00
        if (sorted.get(0).prixDepuisDepart().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessRuleException("Le prix du premier arrêt doit être 0.00");
        }

        // Rule 3: Times and prices must be strictly increasing
        for (int i = 1; i < sorted.size(); i++) {
            if (!sorted.get(i).heurePassage().isAfter(sorted.get(i - 1).heurePassage())) {
                throw new BusinessRuleException("Les heures de passage doivent être strictement croissantes");
            }
            if (sorted.get(i).prixDepuisDepart().compareTo(sorted.get(i - 1).prixDepuisDepart()) <= 0) {
                throw new BusinessRuleException("Les prix doivent être strictement croissants");
            }
        }

        // Rule 4: No duplicate cities
        long uniqueCities = sorted.stream()
                .map(p -> p.ville().toLowerCase())
                .distinct()
                .count();

        if (uniqueCities != sorted.size()) {
            throw new BusinessRuleException("Chaque ville ne peut apparaître qu'une seule fois dans le trajet");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // CORE METHODS
    // ─────────────────────────────────────────────────────────────

    // 1. CREATE TRIP
    @Override
    @Transactional
    public TrajetResponseDto create(TrajetRequestDto requestDto) {
        // 1. Fetch driver and vehicle (or 404)
        Conducteur conducteur = conducteurRepository.findById(requestDto.conducteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur introuvable avec l'id : " + requestDto.conducteurId()));

        Vehicule vehicule = vehiculeRepository.findById(requestDto.vehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id : " + requestDto.vehiculeId()));

        // 2. Run all business rule validations
        validateOwnership(vehicule, requestDto.conducteurId());
        validateCapacity(requestDto.placesProposees(), vehicule.getCapacite());

        List<PointArretRequestDto> sorted = requestDto.pointsArret().stream()
                .sorted(Comparator.comparingInt(PointArretRequestDto::ordre))
                .toList();
        validateStopPoints(sorted);

        // 3. Build and save the Trajet entity
        Trajet trajet = new Trajet();
        trajet.setConducteur(conducteur);
        trajet.setVehicule(vehicule);
        trajet.setDateDepart(requestDto.dateDepart());
        trajet.setPlacesProposees(requestDto.placesProposees());
        trajet.setVilleDepart(sorted.get(0).ville());
        trajet.setVilleArrivee(sorted.get(sorted.size() - 1).ville());
        trajet.setStatut(StatutTrajet.PUBLIE);

        for (PointArretRequestDto dto : sorted) {
            trajet.addPointArret(pointArretMapper.toEntity(dto));
        }

        Trajet saved = trajetRepository.save(trajet);
        log.info("Trajet créé avec succès : ID={}, {} → {}", saved.getId(), saved.getVilleDepart(), saved.getVilleArrivee());

        return trajetMapper.toResponseDto(saved);
    }

    // 2. FIND BY ID (Without N+1 problem!)
    @Override
    @Transactional(readOnly = true)
    public TrajetResponseDto findById(Long id) {
        Trajet trajet = trajetRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable avec l'id : " + id));

        return trajetMapper.toResponseDto(trajet);
    }

    // 3. FIND ALL
    @Override
    @Transactional(readOnly = true)
    public List<TrajetResponseDto> findAll() {
        return trajetRepository.findAll().stream()
                .map(trajetMapper::toResponseDto)
                .toList();
    }

    // 4. TERMINER (SCENARIO 9)
    @Override
    @Transactional
    public TrajetResponseDto terminer(Long id) {
        Trajet trajet = trajetRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable avec l'id : " + id));

        // Trip cannot be already final
        if (trajet.getStatut() == StatutTrajet.TERMINE || trajet.getStatut() == StatutTrajet.ANNULE) {
            throw new BusinessRuleException("Ce trajet ne peut plus être modifié (statut actuel : " + trajet.getStatut() + ")");
        }

        // SCENARIO 9: Cannot terminate before scheduled departure time!
        if (LocalDateTime.now().isBefore(trajet.getDateDepart())) {
            log.warn("Tentative de terminer le trajet #{} avant l'heure de départ prévue", id);
            throw new BusinessRuleException("Le trajet ne peut être terminé qu'après son heure de départ");
        }

        trajet.setStatut(StatutTrajet.TERMINE);
        Trajet saved = trajetRepository.save(trajet);
        log.info("Trajet #{} terminé avec succès", id);

        return trajetMapper.toResponseDto(saved);
    }

    // 5. ANNULER (SCENARIO 11)
    @Override
    @Transactional
    public TrajetResponseDto annuler(Long id) {
        Trajet trajet = trajetRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable avec l'id : " + id));

        // Trip cannot be already final
        if (trajet.getStatut() == StatutTrajet.TERMINE || trajet.getStatut() == StatutTrajet.ANNULE) {
            throw new BusinessRuleException("Ce trajet ne peut plus être modifié (statut actuel : " + trajet.getStatut() + ")");
        }

        trajet.setStatut(StatutTrajet.ANNULE);
        Trajet saved = trajetRepository.save(trajet);
        log.info("Trajet #{} annulé avec succès", id);

        return trajetMapper.toResponseDto(saved);
    }

    // SEARCH TRIPS
    @Override
    @Transactional(readOnly = true)
    public Page<TrajetResponseDto> search(String villeDepart, String villeArrivee, LocalDate date, Pageable pageable) {
        LocalDateTime debutJournee = date.atStartOfDay();
        LocalDateTime finJournee = date.plusDays(1).atStartOfDay();
        Page<Trajet> trajets = trajetRepository.searchTrajets(
                villeDepart,
                villeArrivee,
                StatutTrajet.PUBLIE,
                debutJournee,
                finJournee,
                pageable
        );
        return trajets.map(trajetMapper::toResponseDto);
    }
}