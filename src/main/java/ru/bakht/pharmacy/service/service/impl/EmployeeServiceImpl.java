package ru.bakht.pharmacy.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.EmployeeMapper;
import ru.bakht.pharmacy.service.mapper.PharmacyMapper;
import ru.bakht.pharmacy.service.model.Employee;
import ru.bakht.pharmacy.service.model.Pharmacy;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;
import ru.bakht.pharmacy.service.repository.EmployeeRepository;
import ru.bakht.pharmacy.service.repository.PharmacyRepository;
import ru.bakht.pharmacy.service.service.EmployeeService;

import java.util.List;

/**
 * Реализация интерфейса {@link EmployeeService} для управления сотрудниками.
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
    public EmployeeDto getEmployeeById(Long id) {
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
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        var id = employeeDto.getId();

        if (id != null && employeeRepository.existsById(id)) {
            log.info("Сотрудник с идентификатором {} уже существует, обновление сотрудника", id);
            return updateEmployee(id, employeeDto);
        }

        Pharmacy pharmacy = findPharmacyById(employeeDto.getPharmacy().getId());

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
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        log.info("Обновление сотрудника с идентификатором {}: {}", id, employeeDto);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Сотрудник с идентификатором {} не найден", id);
                    return new EntityNotFoundException("Сотрудник", id);
                });

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
        log.info("Удаление сотрудника с идентификатором {}", id);
        employeeRepository.deleteById(id);
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
                    log.error("Аптека с идентификатором {} не найдена", pharmacyId);
                    return new EntityNotFoundException("Аптека", pharmacyId);
                });
    }
}
