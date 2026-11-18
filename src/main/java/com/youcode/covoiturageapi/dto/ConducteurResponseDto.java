package com.youcode.covoiturageapi.dto;

public record ConducteurResponseDto (
        Long id,
        String nom,
        String prenom,
        String email,
        String telephone,
        String numeroPermis
) {}