package com.youcode.covoiturageapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehiculeRequestDto(

        @NotNull(message = "L'identifiant du conducteur est obligatoire")
        Long conducteurId,
        @NotBlank(message = "La marque est obligatoire")
        String marque,
        @NotBlank(message = "Le modèle est obligatoire")
        String modele,
        @NotBlank(message = "L'immatriculation est obligatoire")
        String immatriculation,
        @NotNull(message = "La capacité est obligatoire")
        @Min(value = 1, message = "La capacité minimale est de 1 place")
        @Max(value = 8, message = "La capacité maximale est de 8 places")
        Integer capacite,
        @NotBlank(message = "La couleur est obligatoire")
        String couleur

) {
}
