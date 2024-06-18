package ru.bakht.pharmacy.service.service;

import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;

import java.util.List;

/**
 * Интерфейс для управления сотрудниками.
 */
public interface EmployeeService {

    /**
     * Возвращает список всех сотрудников.
     *
     * @return список объектов EmployeeDto.
     */
    List<EmployeeDto> getAllEmployees();

    /**
     * Ищет сотрудника по его идентификатору.
     *
     * @param id идентификатор сотрудника.
     * @return объект EmployeeDto, если сотрудник найден.
     * @throws EntityNotFoundException если сотрудник с указанным идентификатором не найден.
     */
    EmployeeDto getEmployeeById(Long id);

    /**
     * Создает нового сотрудника.
     *
     * @param employeeDto данные для создания сотрудника.
     * @return созданный объект EmployeeDto.
     */
    EmployeeDto createEmployee(EmployeeDto employeeDto);

    /**
     * Обновляет существующего сотрудника.
     *
     * @param id идентификатор сотрудника.
     * @param employeeDto данные для обновления сотрудника.
     * @return обновленный объект EmployeeDto.
     * @throws EntityNotFoundException если сотрудник с указанным идентификатором не найден.
     */
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);

    /**
     * Удаляет сотрудника по его идентификатору.
     *
     * @param id идентификатор сотрудника.
     * @throws EntityNotFoundException если сотрудник с указанным идентификатором не найден.
     */
    void deleteEmployeeById(Long id);
}
