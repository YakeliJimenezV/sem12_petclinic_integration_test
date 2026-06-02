package com.tecsup.petclinic.mapper;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface SpecialtyMapper {

    SpecialtyMapper INSTANCE = Mappers.getMapper(SpecialtyMapper.class);

    @Mapping(source = "hOpen",  target = "hOpen")
    @Mapping(source = "hClose", target = "hClose")
    Specialty mapToEntity(SpecialtyDTO dto);

    @Mapping(source = "hOpen",  target = "hOpen")
    @Mapping(source = "hClose", target = "hClose")
    SpecialtyDTO mapToDto(Specialty specialty);

    List<SpecialtyDTO> mapToDtoList(List<Specialty> specialties);

    List<Specialty> mapToEntityList(List<SpecialtyDTO> dtos);

}
