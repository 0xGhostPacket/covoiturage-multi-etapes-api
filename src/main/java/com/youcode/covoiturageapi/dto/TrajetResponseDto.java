package com.youcode.covoiturageapi.dto;

import com.youcode.covoiturageapi.enums.StatutTrajet;

import java.time.LocalDateTime;
import java.util.List;

public record TrajetResponseDto(
        Long id,
        Long conducteurId,
        Long vehiculeId,
        String villeDepart,
        String villeArrivee,
        LocalDateTime dateDepart,
        Integer placesProposees,
        Integer placesRestantes, // Computed dynamically!
        StatutTrajet statut,
        List<PointArretResponseDto> pointsArret
) {}