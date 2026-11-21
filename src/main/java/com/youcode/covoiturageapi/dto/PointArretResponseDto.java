package com.youcode.covoiturageapi.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

public record PointArretResponseDto(
        Long id,
        String ville,
        Integer ordre,
        LocalTime heurePassage,
        BigDecimal prixDepuisDepart
) {}