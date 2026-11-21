package com.youcode.covoiturageapi.mapper;

import com.youcode.covoiturageapi.dto.TrajetResponseDto;
import com.youcode.covoiturageapi.entity.Trajet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PointArretMapper.class})
public interface TrajetMapper {

    @Mapping(source = "conducteur.id", target = "conducteurId")
    @Mapping(source = "vehicule.id", target = "vehiculeId")
    @Mapping(target = "placesRestantes", ignore = true) // Computed dynamically in Service!
    TrajetResponseDto toResponseDto(Trajet trajet);

    List<TrajetResponseDto> toResponseDtoList(List<Trajet> trajets);
}