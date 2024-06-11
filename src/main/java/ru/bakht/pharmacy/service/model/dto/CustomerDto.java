package ru.bakht.pharmacy.service.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDto {

    Long id;

    @NotBlank(message = "Имя обязательно")
    String name;

    @NotBlank(message = "Адрес обязателен")
    String address;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "\\+?\\d{10,15}", message = "Номер телефона должен содержать от 10 до 15 цифр")
    String phone;
}
