package ru.bakht.pharmacy.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    OrderDto toDto(Order order);

    Order toEntity(OrderDto orderDto);

    List<OrderDto> toDtoList(List<Order> orders);

    List<Order> toEntityList(List<OrderDto> orderDtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "pharmacy", ignore = true)
    @Mapping(target = "medication", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    void updateEntityFromDto(OrderDto orderDto, @MappingTarget Order order);

}
