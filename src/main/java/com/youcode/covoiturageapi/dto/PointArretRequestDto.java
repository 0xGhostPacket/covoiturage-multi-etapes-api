package com.youcode.covoiturageapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalTime;

public record PointArretRequestDto(
        @NotBlank(message = "La ville est obligatoire")
        String ville,

        @NotNull(message = "L'ordre est obligatoire")
        @Min(value = 0, message = "L'ordre commence à 0")
        Integer ordre,

        @NotNull(message = "L'heure de passage est obligatoire")
        LocalTime heurePassage,

        @NotNull(message = "Le prix depuis le départ est obligatoire")
        @Min(value = 0, message = "Le prix ne peut pas être négatif")
        BigDecimal prixDepuisDepart
) {}