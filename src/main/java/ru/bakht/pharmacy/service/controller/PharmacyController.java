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
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;
import ru.bakht.pharmacy.service.model.dto.PharmacyMedicationDto;
import ru.bakht.pharmacy.service.service.PharmacyService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pharmacies")
@Tag(name = "Pharmacy Controller", description = "Управление аптеками")
@Validated
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить все аптеки", description = "Возвращает список всех аптек")
    public List<PharmacyDto> getAllPharmacies() {
        log.info("Получен запрос на получение всех аптек");
        List<PharmacyDto> pharmacies = pharmacyService.getAllPharmacies();
        log.info("Возвращено {} аптек", pharmacies.size());
        return pharmacies;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить аптеку по ID", description = "Возвращает аптеку по ее идентификатору")
    public PharmacyDto getPharmacyById(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на получение аптеки с ID {}", id);
        PharmacyDto pharmacy = pharmacyService.getPharmacyById(id);
        log.info("Возвращена аптека: {}", pharmacy);
        return pharmacy;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать новую аптеку", description = "Создает новую аптеку")
    public PharmacyDto createPharmacy(@RequestBody @Valid PharmacyDto pharmacyDto) {
        log.info("Получен запрос на создание аптеки: {}", pharmacyDto);
        PharmacyDto createdPharmacy = pharmacyService.createPharmacy(pharmacyDto);
        log.info("Аптека создана: {}", createdPharmacy);
        return createdPharmacy;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить аптеку", description = "Обновляет существующую аптеку")
    public PharmacyDto updatePharmacy(@PathVariable @Min(1) Long id,
                                      @RequestBody @Valid PharmacyDto pharmacyDto) {
        log.info("Получен запрос на обновление аптеки: {}", pharmacyDto);
        PharmacyDto updatedPharmacy = pharmacyService.updatePharmacy(id, pharmacyDto);
        log.info("Аптека обновлена: {}", updatedPharmacy);
        return updatedPharmacy;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить аптеку", description = "Удаляет аптеку по ее идентификатору")
    public void deletePharmacy(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на удаление аптеки с ID {}", id);
        pharmacyService.deletePharmacyById(id);
        log.info("Аптека с ID {} удалена", id);
    }

    @PostMapping("/medications")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Добавить или обновить лекарство в аптеке", description = "Добавляет новое лекарство в аптеку или обновляет его количество, если связь уже существует")
    public void createMedicationInPharmacy(@RequestBody @Valid PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Получен запрос на создание или обновление лекарства в аптеке: {}", pharmacyMedicationDto);
        pharmacyService.addMedication(pharmacyMedicationDto);
        log.info("Лекарство создано или обновлено в аптеке: {}", pharmacyMedicationDto);
    }

    @PutMapping("/medications")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить лекарство в аптеке", description = "Обновляет количество лекарства в аптеке")
    public void updateMedicationInPharmacy(@RequestBody @Valid PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Получен запрос на обновление лекарства в аптеке: {}", pharmacyMedicationDto);
        pharmacyService.updateMedication(pharmacyMedicationDto);
        log.info("Лекарство обновлено в аптеке: {}", pharmacyMedicationDto);
    }

    @DeleteMapping("/medications")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить лекарство из аптеки", description = "Удаляет лекарство из аптеки")
    public void removeMedicationFromPharmacy(@RequestBody @Valid PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Получен запрос на удаление лекарства из аптеки: {}", pharmacyMedicationDto);
        pharmacyService.removeMedication(pharmacyMedicationDto);
        log.info("Лекарство удалено из аптеки: {}", pharmacyMedicationDto);
    }
}
