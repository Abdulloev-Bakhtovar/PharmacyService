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
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.service.MedicationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medications")
@Tag(name = "Medication Controller", description = "Управление лекарствами")
@Validated
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить все лекарства", description = "Возвращает список всех лекарств")
    public List<MedicationDto> getAllMedications() {
        log.info("Получен запрос на получение всех лекарств");
        List<MedicationDto> medications = medicationService.getAllMedications();
        log.info("Возвращено {} лекарств", medications.size());
        return medications;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить лекарство по ID", description = "Возвращает лекарство по его идентификатору")
    public MedicationDto getMedicationById(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на получение лекарства с ID {}", id);
        MedicationDto medication = medicationService.getMedicationById(id);
        log.info("Возвращено лекарство: {}", medication);
        return medication;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать новое лекарство", description = "Создает новое лекарство")
    public MedicationDto createMedication(@RequestBody @Valid MedicationDto medicationDto) {
        log.info("Получен запрос на создание лекарства: {}", medicationDto);
        MedicationDto createdMedication = medicationService.createMedication(medicationDto);
        log.info("Лекарство создано: {}", createdMedication);
        return createdMedication;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить лекарство", description = "Обновляет существующее лекарство")
    public MedicationDto updateMedication(@PathVariable @Min(1) Long id,
                                          @RequestBody @Valid MedicationDto medicationDto) {
        log.info("Получен запрос на обновление лекарства: {}", medicationDto);
        MedicationDto updatedMedication = medicationService.updateMedication(id, medicationDto);
        log.info("Лекарство обновлено: {}", updatedMedication);
        return updatedMedication;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить лекарство", description = "Удаляет лекарство по его идентификатору")
    public void deleteMedication(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на удаление лекарства с ID {}", id);
        medicationService.deleteMedicationById(id);
        log.info("Лекарство с ID {} удалено", id);
    }
}
