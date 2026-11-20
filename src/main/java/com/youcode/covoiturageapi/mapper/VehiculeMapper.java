package com.youcode.covoiturageapi.mapper;

import com.youcode.covoiturageapi.dto.VehiculeRequestDto;
import com.youcode.covoiturageapi.dto.VehiculeResponseDto;
import com.youcode.covoiturageapi.entity.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {

    @Mapping(target = "conducteur", ignore = true)
    Vehicule toEntity(VehiculeRequestDto requestDto);

    @Mapping(source = "conducteur.id", target = "conducteurId")
    VehiculeResponseDto toResponseDto(Vehicule vehicule);

    List<VehiculeResponseDto> toResponseDtoList(List<Vehicule> vehicules);
}