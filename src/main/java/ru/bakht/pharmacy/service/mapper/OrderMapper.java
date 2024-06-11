package ru.bakht.pharmacy.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bakht.pharmacy.service.model.Order;
import ru.bakht.pharmacy.service.model.dto.OrderDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        CustomerMapper.class,
        EmployeeMapper.class,
        PharmacyMapper.class,
        MedicationMapper.class
})
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerDto.id")
    @Mapping(source = "employee.id", target = "employeeDto.id")
    @Mapping(source = "pharmacy.id", target = "pharmacyDto.id")
    @Mapping(source = "medication.id", target = "medicationDto.id")
    OrderDto toDto(Order order);

    @Mapping(source = "customerDto.id", target = "customer.id")
    @Mapping(source = "employeeDto.id", target = "employee.id")
    @Mapping(source = "pharmacyDto.id", target = "pharmacy.id")
    @Mapping(source = "medicationDto.id", target = "medication.id")
    Order toEntity(OrderDto orderDto);

    List<OrderDto> toDtoList(List<Order> orders);

    List<Order> toEntityList(List<OrderDto> orderDtos);

}
