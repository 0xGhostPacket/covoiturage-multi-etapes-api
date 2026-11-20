package com.youcode.covoiturageapi.dto;

public record VehiculeResponseDto(
        Long id,
        String marque,
        String modele,
        String immatriculation,
        String couleur,
        Integer capacite,
        Long conducteurId
) {
}
