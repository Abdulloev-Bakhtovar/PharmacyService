package ru.bakht.pharmacy.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.EmployeeMapper;
import ru.bakht.pharmacy.service.mapper.PharmacyMapper;
import ru.bakht.pharmacy.service.model.Employee;
import ru.bakht.pharmacy.service.model.Pharmacy;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;
import ru.bakht.pharmacy.service.repository.EmployeeRepository;
import ru.bakht.pharmacy.service.specification.EmployeeSpecification;

import java.util.List;

/**
 * Реализация интерфейса {@link BaseService } для управления сотрудниками.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService implements BaseService<EmployeeDto, Long> {

    private final EmployeeRepository employeeRepository;
    private final PharmacyService pharmacyService;
    private final EmployeeMapper employeeMapper;
    private final PharmacyMapper pharmacyMapper;


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAll() {
        log.info("Получение всех сотрудников");
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getById(Long id) {
        log.info("Получение сотрудника с идентификатором {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Сотрудник с идентификатором {} не найден", id);
                    return new EntityNotFoundException("Сотрудник", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        var id = employeeDto.getId();

        if (id != null && employeeRepository.existsById(id)) {
            log.info("Сотрудник с идентификатором {} уже существует, обновление сотрудника", id);
            return update(id, employeeDto);
        }

        Pharmacy pharmacy = pharmacyMapper.toEntity(
                pharmacyService.getById(employeeDto.getPharmacy().getId())
        );

        log.info("Создание нового сотрудника: {}", employeeDto);
        Employee employee = employeeMapper.toEntity(employeeDto);
        employee.setPharmacy(pharmacy);
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        log.info("Обновление сотрудника с идентификатором {}: {}", id, employeeDto);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Сотрудник с идентификатором {} не найден", id);
                    return new EntityNotFoundException("Сотрудник", id);
                });

        Pharmacy pharmacy = pharmacyMapper.toEntity(
                pharmacyService.getById(employeeDto.getPharmacy().getId())
        );

        employeeMapper.updateEntityFromDto(employeeDto, existingEmployee);
        existingEmployee.setPharmacy(pharmacy);
        return employeeMapper.toDto(employeeRepository.save(existingEmployee));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        log.info("Удаление сотрудника с идентификатором {}", id);
        employeeRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public List<EmployeeDto> getByFilters(EmployeeDto employeeDto) {
        log.info("Фильтрация сотрудников по заданным критериям");
        Specification<Employee> spec = Specification.where(EmployeeSpecification.hasName(employeeDto.getName()))
                .and(EmployeeSpecification.hasPosition(employeeDto.getPosition()))
                .and(EmployeeSpecification.hasEmail(employeeDto.getEmail()))
                .and(EmployeeSpecification.hasPharmacyId(employeeDto.getPharmacy() != null ?
                        employeeDto.getPharmacy().getId() : null));

        return employeeRepository.findAll(spec).stream()
                .map(employeeMapper::toDto)
                .toList();
    }

}
