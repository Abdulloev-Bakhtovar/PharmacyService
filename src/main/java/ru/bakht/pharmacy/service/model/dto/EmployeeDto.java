package ru.bakht.pharmacy.service.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bakht.pharmacy.service.enums.EmployeePosition;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDto {

    Long id;

    @NotBlank(message = "Имя обязательно")
    String name;

    @NotNull(message = "Должность обязательна")
    EmployeePosition employeePosition;

    @Email(message = "Электронная почта должна быть действительной")
    String email;

    @NotNull(message = "Аптека обязательна")
    PharmacyDto pharmacy;
}
