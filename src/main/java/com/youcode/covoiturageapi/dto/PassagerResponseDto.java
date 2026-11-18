package com.youcode.covoiturageapi.dto;

public record PassagerResponseDto (
        Long id,
        String nom,
        String prenom,
        String email,
        String telephone
){
}
