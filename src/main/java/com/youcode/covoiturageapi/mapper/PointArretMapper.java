package com.youcode.covoiturageapi.mapper;

import com.youcode.covoiturageapi.dto.PointArretRequestDto;
import com.youcode.covoiturageapi.dto.PointArretResponseDto;
import com.youcode.covoiturageapi.entity.PointArret;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PointArretMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trajet", ignore = true)
    
    PointArret toEntity(PointArretRequestDto requestDto);
    PointArretResponseDto toResponseDto(PointArret pointArret);
    List<PointArretResponseDto> toResponseDtoList(List<PointArret> pointsArret);
}