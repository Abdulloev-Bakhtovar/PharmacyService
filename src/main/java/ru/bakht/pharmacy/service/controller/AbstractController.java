package ru.bakht.pharmacy.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bakht.pharmacy.service.service.BaseService;

import java.util.List;

@RequiredArgsConstructor
@Validated
public abstract class AbstractController<T, ID extends Number> {

    private final BaseService<T, ID> baseService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить все сущности", description = "Возвращает список всех сущностей")
    public List<T> getAll() {
        return baseService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить сущность по ID", description = "Возвращает сущность по ее идентификатору")
    public T getById(@PathVariable @Min(1) ID id) {
        return baseService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать новую сущность", description = "Создает новую сущность")
    public T create(@RequestBody @Valid T dto) {
        return baseService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить сущность", description = "Обновляет существующую сущность")
    public T update(@PathVariable @Min(1) ID id, @RequestBody @Valid T dto) {
        return baseService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить сущность", description = "Удаляет сущность по ее идентификатору")
    public void delete(@PathVariable @Min(1) ID id) {
        baseService.delete(id);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить сущности по фильтрам", description = "Возвращает список сущностей по заданным фильтрам")
    public List<T> getByFilters(@RequestBody T filters) {
        return baseService.getByFilters(filters);
    }
}
