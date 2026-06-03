package com.tecsup.petclinic.mapper;


import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    SpecialtyMapper INSTANCE = Mappers.getMapper(SpecialtyMapper.class);

    default Specialty mapToEntity(SpecialtyDTO dto) {
        if (dto == null) return null;
        Specialty s = new Specialty();
        s.setId(dto.getId());
        s.setName(dto.getName());
        s.setOffice(dto.getOffice());
        s.setHOpen(dto.getHOpen());
        s.setHClose(dto.getHClose());
        return s;
    }

    default SpecialtyDTO mapToDto(Specialty s) {
        if (s == null) return null;
        SpecialtyDTO dto = new SpecialtyDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setOffice(s.getOffice());
        dto.setHOpen(s.getHOpen());
        dto.setHClose(s.getHClose());
        return dto;
    }

    default List<SpecialtyDTO> mapToDtoList(List<Specialty> list) {
        if (list == null) return null;
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    default List<Specialty> mapToEntityList(List<SpecialtyDTO> list) {
        if (list == null) return null;
        return list.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}