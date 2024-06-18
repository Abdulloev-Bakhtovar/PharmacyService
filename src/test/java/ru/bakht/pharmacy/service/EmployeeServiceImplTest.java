package ru.bakht.pharmacy.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bakht.pharmacy.service.enums.Position;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.EmployeeMapper;
import ru.bakht.pharmacy.service.mapper.PharmacyMapper;
import ru.bakht.pharmacy.service.model.Employee;
import ru.bakht.pharmacy.service.model.Pharmacy;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;
import ru.bakht.pharmacy.service.repository.EmployeeRepository;
import ru.bakht.pharmacy.service.service.PharmacyService;
import ru.bakht.pharmacy.service.service.impl.EmployeeServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PharmacyService pharmacyService;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private PharmacyMapper pharmacyMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("getAllEmployees returns list of all employees")
    void getAllEmployees_ReturnsAllEmployees() {
        Employee employee = new Employee(1L, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", new Pharmacy());
        EmployeeDto employeeDto = new EmployeeDto(1L, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", new PharmacyDto());

        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeDto);

        List<EmployeeDto> employees = employeeService.getAllEmployees();

        assertEquals(1, employees.size());
        assertEquals(1L, employees.getFirst().getId());
        assertEquals("Ivan Ivanov", employees.getFirst().getName());
        assertEquals(Position.PHARMACIST, employees.getFirst().getPosition());
        assertEquals("ivanov@example.com", employees.getFirst().getEmail());

        verify(employeeRepository, times(1)).findAll();
        verify(employeeMapper, times(1)).toDto(any(Employee.class));
    }

    @Test
    void getEmployeeById_ReturnsEmployee() {
        Employee employee = new Employee(1L, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", new Pharmacy());
        EmployeeDto employeeDto = new EmployeeDto(1L, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", new PharmacyDto());

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeDto);

        EmployeeDto foundEmployee = employeeService.getEmployeeById(1L);

        assertEquals(1L, foundEmployee.getId());
        assertEquals("Ivan Ivanov", foundEmployee.getName());
        assertEquals(Position.PHARMACIST, foundEmployee.getPosition());
        assertEquals("ivanov@example.com", foundEmployee.getEmail());

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeMapper, times(1)).toDto(any(Employee.class));
    }

    @Test
    void getEmployeeById_ReturnsEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.getEmployeeById(1L),
                "Expected getEmployeeById to throw, but it didn't"
        );

        assertEquals("Сотрудник с ID 1 не найден", thrown.getMessage());

        verify(employeeRepository, times(1)).findById(1L);
        verifyNoInteractions(employeeMapper);
    }


    @Test
    void createEmployee_ReturnsEmployee() {
        Pharmacy pharmacy = new Pharmacy(1L, "Pharmacy A", "123 Street", "123456789", null);
        PharmacyDto pharmacyDto = new PharmacyDto(1L, "Pharmacy A", "123 Street", "123456789");
        Employee employee = new Employee(null, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", pharmacy);
        Employee savedEmployee = new Employee(1L, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", pharmacy);
        EmployeeDto employeeDto = new EmployeeDto(null, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", pharmacyDto);
        EmployeeDto savedEmployeeDto = new EmployeeDto(1L, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", pharmacyDto);

        when(employeeMapper.toEntity(any(EmployeeDto.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(savedEmployeeDto);
        when(pharmacyService.getPharmacyById(1L)).thenReturn(pharmacyDto);

        EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);

        assertEquals(1L, createdEmployee.getId());
        assertEquals("Ivan Ivanov", createdEmployee.getName());
        assertEquals(Position.PHARMACIST, createdEmployee.getPosition());
        assertEquals("ivanov@example.com", createdEmployee.getEmail());

        verify(employeeMapper, times(1)).toEntity(any(EmployeeDto.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toDto(any(Employee.class));
        verify(pharmacyService, times(1)).getPharmacyById(1L);
    }


    @Test
    void updateEmployee_ReturnsEmployee() {
        Pharmacy pharmacy = new Pharmacy(1L, "Pharmacy A", "123 Street", "123456789", null);
        PharmacyDto pharmacyDto = new PharmacyDto(1L, "Pharmacy A", "123 Street", "123456789");
        Employee existingEmployee = new Employee(1L, "Ivan Ivanov", Position.PHARMACIST, "ivanov@example.com", pharmacy);
        Employee updatedEmployee = new Employee(1L, "Petr Petrov", Position.MANAGER, "petrov@example.com", pharmacy);
        EmployeeDto employeeDto = new EmployeeDto(1L, "Petr Petrov", Position.MANAGER, "petrov@example.com", pharmacyDto);
        EmployeeDto updatedEmployeeDto = new EmployeeDto(1L, "Petr Petrov", Position.MANAGER, "petrov@example.com", pharmacyDto);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(updatedEmployeeDto);
        when(pharmacyService.getPharmacyById(1L)).thenReturn(pharmacyDto);

        EmployeeDto result = employeeService.updateEmployee(1L, employeeDto);

        assertEquals("Petr Petrov", result.getName());
        assertEquals(Position.MANAGER, result.getPosition());
        assertEquals("petrov@example.com", result.getEmail());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeMapper, times(1)).toDto(any(Employee.class));
    }


    @Test
    void deleteCustomerById_SuccessfulDeletion() {
        Long employeeId = 1L;

        employeeService.deleteEmployeeById(employeeId);

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
