package ru.bakht.pharmacy.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;
import ru.bakht.pharmacy.service.model.dto.PharmacyMedicationDto;
import ru.bakht.pharmacy.service.service.PharmacyService;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController extends AbstractController<PharmacyDto, Long> {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        super(pharmacyService);
        this.pharmacyService = pharmacyService;
    }

    @PostMapping("/medications")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Добавить или обновить лекарство в аптеке",
            description = "Добавляет новое лекарство в аптеку или обновляет его количество, если связь уже существует")
    public void addOrUpdatePharmacyMedication(@RequestBody @Valid PharmacyMedicationDto pharmacyMedicationDto) {
        pharmacyService.addOrUpdatePharmacyMedication(pharmacyMedicationDto);
    }

    @DeleteMapping("/medications")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить лекарство из аптеки", description = "Удаляет лекарство из аптеки")
    public void deletePharmacyMedication(@RequestBody @Valid PharmacyMedicationDto pharmacyMedicationDto) {
        pharmacyService.deletePharmacyMedication(pharmacyMedicationDto);
    }
}