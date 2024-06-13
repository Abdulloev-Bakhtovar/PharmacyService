package ru.bakht.pharmacy.service.mapper;

import org.mapstruct.Mapper;
import ru.bakht.pharmacy.service.model.Employee;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto toDto(Employee employee);

    Employee toEntity(EmployeeDto employeeDto);

    List<EmployeeDto> toDtoList(List<Employee> employees);

    List<Employee> toEntityList(List<EmployeeDto> employeeDtos);
}
