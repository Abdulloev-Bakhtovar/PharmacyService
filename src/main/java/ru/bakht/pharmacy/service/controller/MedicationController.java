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
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.service.MedicationService;

import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@RequestMapping("/api/medications")
@Tag(name = "Medication Controller", description = "Управление лекарствами")
@Validated
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить все лекарства", description = "Возвращает список всех лекарств")
    public List<MedicationDto> getAllMedications() {
        return medicationService.getAllMedications();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить лекарство по ID", description = "Возвращает лекарство по его идентификатору")
    public MedicationDto getMedicationById(@PathVariable @Min(1) Long id) {
        return medicationService.getMedicationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать новое лекарство", description = "Создает новое лекарство")
    public MedicationDto createMedication(@RequestBody @Valid MedicationDto medicationDto) {
        return medicationService.createMedication(medicationDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить лекарство", description = "Обновляет существующее лекарство")
    public MedicationDto updateMedication(@PathVariable @Min(1) Long id,
                                          @RequestBody @Valid MedicationDto medicationDto) {
        return medicationService.updateMedication(id, medicationDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить лекарство", description = "Удаляет лекарство по его идентификатору")
    public void deleteMedication(@PathVariable @Min(1) Long id) {
        medicationService.deleteMedicationById(id);
    }
}
