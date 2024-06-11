package ru.bakht.pharmacy.service.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PharmacyMedicationDto {

    @NotNull(message = "Аптека обязательна")
    PharmacyDto pharmacyDto;

    @NotNull(message = "Лекарство обязательно")
    MedicationDto medicationDto;

    @NotNull(message = "Количество обязательно")
    @Min(value = 1, message = "Количество должно быть не менее 1")
    Integer quantity;
}
