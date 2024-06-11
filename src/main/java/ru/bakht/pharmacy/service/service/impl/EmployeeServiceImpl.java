package ru.bakht.pharmacy.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.EmployeeMapper;
import ru.bakht.pharmacy.service.model.Employee;
import ru.bakht.pharmacy.service.model.Pharmacy;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;
import ru.bakht.pharmacy.service.repository.EmployeeRepository;
import ru.bakht.pharmacy.service.repository.PharmacyRepository;
import ru.bakht.pharmacy.service.service.EmployeeService;

import java.util.List;

/**
 * Реализация интерфейса EmployeeService.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PharmacyRepository pharmacyRepository;
    private final EmployeeMapper employeeMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long id) {
        log.info("Fetching employee with id {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Employee with id {} not found", id);
                    return new EntityNotFoundException("Сотрудник", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Creating new employee: {}", employeeDto);

        if (employeeDto.getPharmacy().getId() == null) {
            throw new EntityNotFoundException("Аптека", null);
        }

        Pharmacy pharmacy = findPharmacyById(employeeDto.getPharmacy().getId());

        Employee employee = employeeMapper.toEntity(employeeDto);
        employee.setPharmacy(pharmacy);
        employee = employeeRepository.save(employee);

        log.info("Created employee: {}", employee);
        return employeeMapper.toDto(employee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        log.info("Updating employee with id {}: {}", id, employeeDto);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee with id {} not found", id);
                    return new EntityNotFoundException("Сотрудник", id);
                });

        if (employeeDto.getPharmacy().getId() == null) {
            throw new EntityNotFoundException("Аптека", null);
        }

        Pharmacy pharmacy = findPharmacyById(employeeDto.getPharmacy().getId());

        updateEmployeeFromDto(existingEmployee, employeeDto);
        existingEmployee.setPharmacy(pharmacy);

        return employeeMapper.toDto(employeeRepository.save(existingEmployee));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEmployeeById(Long id) {
        log.info("Deleting employee with id {}", id);
        employeeRepository.findById(id).ifPresentOrElse(
                employee -> {
                    employeeRepository.deleteById(id);
                    log.info("Deleted employee with id {}", id);
                },
                () -> {
                    log.error("Employee with id {} not found", id);
                    throw new EntityNotFoundException("Сотрудник", id);
                }
        );
    }

    /**
     * Обновляет информацию о сотруднике на основе данных из DTO.
     *
     * @param employee объект Employee, который необходимо обновить
     * @param employeeDto объект EmployeeDto с новыми данными
     */
    private void updateEmployeeFromDto(Employee employee, EmployeeDto employeeDto) {
        employee.setName(employeeDto.getName());
        employee.setPosition(employeeDto.getPosition());
        employee.setEmail(employeeDto.getEmail());
    }

    /**
     * Находит аптеку по идентификатору и выбрасывает исключение, если аптека не найдена.
     *
     * @param pharmacyId идентификатор аптеки
     * @return найденная аптека
     * @throws EntityNotFoundException если аптека не найдена
     */
    private Pharmacy findPharmacyById(Long pharmacyId) {
        return pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> {
                    log.error("Pharmacy with id {} not found", pharmacyId);
                    return new EntityNotFoundException("Аптека", pharmacyId);
                });
    }
}
