package ru.bakht.pharmacy.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;
import ru.bakht.pharmacy.service.service.EmployeeService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
@Tag(name = "Employee Controller", description = "Управление сотрудниками")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить всех сотрудников", description = "Возвращает список всех сотрудников")
    public List<EmployeeDto> getAllEmployees() {
        log.info("Получен запрос на получение всех сотрудников");
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        log.info("Возвращено {} сотрудников", employees.size());
        return employees;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить сотрудника по ID", description = "Возвращает сотрудника по его идентификатору")
    public EmployeeDto getEmployeeById(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на получение сотрудника с ID {}", id);
        EmployeeDto employee = employeeService.getEmployeeById(id);
        log.info("Возвращен сотрудник: {}", employee);
        return employee;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать нового сотрудника", description = "Создает нового сотрудника")
    public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
        log.info("Получен запрос на создание сотрудника: {}", employeeDto);
        EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);
        log.info("Сотрудник создан: {}", createdEmployee);
        return createdEmployee;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить сотрудника", description = "Обновляет существующего сотрудника")
    public EmployeeDto updateEmployee(@PathVariable @Min(1) Long id,
                                      @RequestBody @Valid EmployeeDto employeeDto) {
        log.info("Получен запрос на обновление сотрудника: {}", employeeDto);
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        log.info("Сотрудник обновлен: {}", updatedEmployee);
        return updatedEmployee;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить сотрудника", description = "Удаляет сотрудника по его идентификатору")
    public void deleteEmployee(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на удаление сотрудника с ID {}", id);
        employeeService.deleteEmployeeById(id);
        log.info("Сотрудник с ID {} удален", id);
    }
}
