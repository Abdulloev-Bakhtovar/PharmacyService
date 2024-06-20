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
import ru.bakht.pharmacy.service.model.dto.CustomerDto;
import ru.bakht.pharmacy.service.service.CustomerService;

import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@Tag(name = "Customer Controller", description = "Управление клиентами")
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить всех клиентов", description = "Возвращает список всех клиентов")
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить клиента по ID", description = "Возвращает клиента по его идентификатору")
    public CustomerDto getCustomerById(@PathVariable @Min(1) Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать нового клиента", description = "Создает нового клиента")
    public CustomerDto createCustomer(@RequestBody @Valid CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить клиента", description = "Обновляет существующего клиента")
    public CustomerDto updateCustomer(@PathVariable @Min(1) Long id,
                                      @RequestBody @Valid CustomerDto customerDto) {
        return customerService.updateCustomer(id, customerDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по его идентификатору")
    public void deleteCustomer(@PathVariable @Min(1) Long id) {
        customerService.deleteCustomerById(id);
    }
}
