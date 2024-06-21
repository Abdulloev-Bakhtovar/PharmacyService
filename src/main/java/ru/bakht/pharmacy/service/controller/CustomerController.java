package ru.bakht.pharmacy.service.controller;

import org.springframework.web.bind.annotation.*;
import ru.bakht.pharmacy.service.model.dto.CustomerDto;
import ru.bakht.pharmacy.service.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController extends AbstractController<CustomerDto, Long> {

    public CustomerController(CustomerService customerService) {
        super(customerService);
    }
}