package com.youcode.covoiturageapi.mapper;

import com.youcode.covoiturageapi.dto.ConducteurRequestDto;
import com.youcode.covoiturageapi.dto.ConducteurResponseDto;
import com.youcode.covoiturageapi.entity.Conducteur;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConducteurMapper {

    Conducteur toEntity(ConducteurRequestDto requestDto);
    ConducteurResponseDto toResponseDto(Conducteur conducteur);
    List<ConducteurResponseDto> toResponseDtoList(List<Conducteur> conductors);
}
