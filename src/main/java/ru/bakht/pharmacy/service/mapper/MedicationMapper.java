package ru.bakht.pharmacy.service.mapper;

import org.mapstruct.Mapper;
import ru.bakht.pharmacy.service.model.Medication;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    MedicationDto toDto(Medication medication);

    Medication toEntity(MedicationDto medicationDto);

    List<MedicationDto> toDtoList(List<Medication> medications);

    List<Medication> toEntityList(List<MedicationDto> medicationDtos);
}
