package ru.bakht.pharmacy.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalOrders {

    Integer totalQuantity;
    Double totalAmount;
}