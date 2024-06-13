package ru.bakht.pharmacy.service.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bakht.pharmacy.service.model.enums.Status;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {

    Long id;

    @NotNull(message = "Покупатель обязателен")
    CustomerDto customerDto;

    @NotNull(message = "Сотрудник обязателен")
    EmployeeDto employeeDto;

    @NotNull(message = "Аптека обязательна")
    PharmacyDto pharmacyDto;

    @NotNull(message = "Лекарство обязательно")
    MedicationDto medicationDto;

    @NotNull(message = "Количество обязательно")
    @Min(value = 1, message = "Количество должно быть не менее 1")
    Integer quantity;

    @NotNull(message = "Общая сумма обязательна")
    Double totalAmount;

    @NotNull(message = "Дата заказа обязательна")
    LocalDate orderDate;

    @NotNull(message = "Статус обязателен")
    Status status;
}
