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
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;
import ru.bakht.pharmacy.service.model.dto.PharmacyMedicationDto;
import ru.bakht.pharmacy.service.service.PharmacyService;

import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@RequestMapping("/api/pharmacies")
@Tag(name = "Pharmacy Controller", description = "Управление аптеками")
@Validated
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить все аптеки", description = "Возвращает список всех аптек")
    public List<PharmacyDto> getAllPharmacies() {
        return pharmacyService.getAllPharmacies();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить аптеку по ID", description = "Возвращает аптеку по ее идентификатору")
    public PharmacyDto getPharmacyById(@PathVariable @Min(1) Long id) {
        return pharmacyService.getPharmacyById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать новую аптеку", description = "Создает новую аптеку")
    public PharmacyDto createPharmacy(@RequestBody @Valid PharmacyDto pharmacyDto) {
        return pharmacyService.createPharmacy(pharmacyDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить аптеку", description = "Обновляет существующую аптеку")
    public PharmacyDto updatePharmacy(@PathVariable @Min(1) Long id,
                                      @RequestBody @Valid PharmacyDto pharmacyDto) {
        return pharmacyService.updatePharmacy(id, pharmacyDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить аптеку", description = "Удаляет аптеку по ее идентификатору")
    public void deletePharmacy(@PathVariable @Min(1) Long id) {
        pharmacyService.deletePharmacyById(id);
    }

    @PostMapping("/medications")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Добавить или обновить лекарство в аптеке",
            description = "Добавляет новое лекарство в аптеку или обновляет его количество, если связь уже существует")
    public void addOrUpdateMedication(@RequestBody @Valid PharmacyMedicationDto pharmacyMedicationDto) {
        pharmacyService.addOrUpdateMedication(pharmacyMedicationDto);
    }

    @DeleteMapping("/medications")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить лекарство из аптеки", description = "Удаляет лекарство из аптеки")
    public void removeMedication(@RequestBody @Valid PharmacyMedicationDto pharmacyMedicationDto) {
        pharmacyService.removeMedication(pharmacyMedicationDto);
    }
}
