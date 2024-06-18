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
import ru.bakht.pharmacy.service.model.dto.CustomerDto;
import ru.bakht.pharmacy.service.service.CustomerService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@Tag(name = "Customer Controller", description = "Управление клиентами")
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить всех клиентов", description = "Возвращает список всех клиентов")
    public List<CustomerDto> getAllCustomers() {
        log.info("Получен запрос на получение всех клиентов");
        List<CustomerDto> customers = customerService.getAllCustomers();
        log.info("Возвращено {} клиентов", customers.size());
        return customers;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить клиента по ID", description = "Возвращает клиента по его идентификатору")
    public CustomerDto getCustomerById(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на получение клиента с ID {}", id);
        CustomerDto customer = customerService.getCustomerById(id);
        log.info("Возвращен клиент: {}", customer);
        return customer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать нового клиента", description = "Создает нового клиента")
    public CustomerDto createCustomer(@RequestBody @Valid CustomerDto customerDto) {
        log.info("Получен запрос на создание клиента: {}", customerDto);
        CustomerDto createdCustomer = customerService.createCustomer(customerDto);
        log.info("Клиент создан: {}", createdCustomer);
        return createdCustomer;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить клиента", description = "Обновляет существующего клиента")
    public CustomerDto updateCustomer(@PathVariable @Min(1) Long id,
                                      @RequestBody @Valid CustomerDto customerDto) {
        log.info("Получен запрос на обновление клиента: {}", customerDto);
        CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto);
        log.info("Клиент обновлен: {}", updatedCustomer);
        return updatedCustomer;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по его идентификатору")
    public void deleteCustomer(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на удаление клиента с ID {}", id);
        customerService.deleteCustomerById(id);
        log.info("Клиент с ID {} удален", id);
    }
}
