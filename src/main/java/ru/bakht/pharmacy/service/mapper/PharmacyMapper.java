package ru.bakht.pharmacy.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bakht.pharmacy.service.model.Order;
import ru.bakht.pharmacy.service.model.Pharmacy;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PharmacyMapper {

    PharmacyDto toDto(Pharmacy pharmacy);

    Pharmacy toEntity(PharmacyDto pharmacyDto);

    List<PharmacyDto> toDtoList(List<Pharmacy> pharmacies);

    List<Pharmacy> toEntityList(List<PharmacyDto> pharmacyDtos);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PharmacyDto pharmacyDto, @MappingTarget Pharmacy pharmacy);
}
