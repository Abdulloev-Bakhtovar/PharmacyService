package ru.bakht.pharmacy.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.service.MedicationService;

@RestController
@RequestMapping("/api/medications")
public class MedicationController extends AbstractController<MedicationDto, Long> {

    public MedicationController(MedicationService medicationService) {
        super(medicationService);
    }
}
