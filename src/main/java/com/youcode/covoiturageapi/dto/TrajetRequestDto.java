package com.youcode.covoiturageapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record TrajetRequestDto(
        @NotNull(message = "L'identifiant du conducteur est obligatoire")
        Long conducteurId,

        @NotNull(message = "L'identifiant du véhicule est obligatoire")
        Long vehiculeId,

        @NotNull(message = "La date de départ est obligatoire")
        @Future(message = "La date de départ doit être dans le futur")
        LocalDateTime dateDepart,

        @NotNull(message = "Le nombre de places proposées est obligatoire")
        @Min(value = 1, message = "Au moins 1 place doit être proposée")
        Integer placesProposees,

        @NotEmpty(message = "Le trajet doit comporter au moins des points d'arrêt")
        @Valid
        List<PointArretRequestDto> pointsArret
) {}