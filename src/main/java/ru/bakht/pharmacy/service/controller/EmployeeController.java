package ru.bakht.pharmacy.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;
import ru.bakht.pharmacy.service.service.EmployeeService;

import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@RequestMapping("/api/employees")
@Tag(name = "Employee Controller", description = "Управление сотрудниками")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить всех сотрудников", description = "Возвращает список всех сотрудников")
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить сотрудника по ID", description = "Возвращает сотрудника по его идентификатору")
    public EmployeeDto getEmployeeById(@PathVariable @Min(1) Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать нового сотрудника", description = "Создает нового сотрудника")
    public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить сотрудника", description = "Обновляет существующего сотрудника")
    public EmployeeDto updateEmployee(@PathVariable @Min(1) Long id,
                                      @RequestBody @Valid EmployeeDto employeeDto) {
        return employeeService.updateEmployee(id, employeeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить сотрудника", description = "Удаляет сотрудника по его идентификатору")
    public void deleteEmployee(@PathVariable @Min(1) Long id) {
        employeeService.deleteEmployeeById(id);
    }
}
