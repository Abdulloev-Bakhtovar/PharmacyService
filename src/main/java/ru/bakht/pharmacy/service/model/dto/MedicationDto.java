package ru.bakht.pharmacy.service.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bakht.pharmacy.service.model.enums.Form;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicationDto {

    Long id;

    @NotBlank(message = "Название обязательно")
    String name;

    @NotNull(message = "Форма обязательна")
    Form form;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше нуля")
    Double price;

    @NotNull(message = "Дата истечения срока обязательна")
    Date expirationDate;


}
