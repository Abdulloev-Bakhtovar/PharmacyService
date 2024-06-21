package ru.bakht.pharmacy.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController extends AbstractController<OrderDto, Long> {

    public OrderController(OrderService orderService) {
        super(orderService);
    }
}
