package ru.bakht.pharmacy.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bakht.pharmacy.service.model.Customer;
import ru.bakht.pharmacy.service.model.dto.CustomerDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto customerDto);

    List<CustomerDto> toDtoList(List<Customer> customers);

    List<Customer> toEntityList(List<CustomerDto> customerDtos);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CustomerDto customerDto, @MappingTarget Customer customer);
}
