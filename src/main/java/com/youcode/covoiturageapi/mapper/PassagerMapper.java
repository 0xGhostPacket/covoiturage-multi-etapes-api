package com.youcode.covoiturageapi.mapper;

import com.youcode.covoiturageapi.dto.PassagerRequestDto;
import com.youcode.covoiturageapi.dto.PassagerResponseDto;
import com.youcode.covoiturageapi.entity.Passager;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PassagerMapper {

    Passager toEntity(PassagerRequestDto requestDto);
    PassagerResponseDto toResponseDto(Passager passager);
    List<PassagerResponseDto> toResponseDtoList(List<Passager> passagers);
}
