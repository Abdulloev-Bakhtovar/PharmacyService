package ru.bakht.pharmacy.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bakht.pharmacy.service.model.dto.EmployeeDto;
import ru.bakht.pharmacy.service.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController extends AbstractController<EmployeeDto, Long> {

    public EmployeeController(EmployeeService employeeService) {
        super(employeeService);
    }
}