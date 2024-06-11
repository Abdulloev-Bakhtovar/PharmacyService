package ru.bakht.pharmacy.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Order Controller", description = "Управление заказами")
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить все заказы", description = "Возвращает список всех заказов")
    public List<OrderDto> getAllOrders() {
        log.info("Получен запрос на получение всех заказов");
        List<OrderDto> orders = orderService.getAllOrders();
        log.info("Возвращено {} заказов", orders.size());
        return orders;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить заказ по ID", description = "Возвращает заказ по его идентификатору")
    public OrderDto getOrderById(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на получение заказа с ID {}", id);
        OrderDto order = orderService.getOrderById(id);
        log.info("Возвращен заказ: {}", order);
        return order;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Создать новый заказ", description = "Создает новый заказ")
    public OrderDto createOrder(@RequestBody @Valid OrderDto orderDto) {
        log.info("Получен запрос на создание заказа: {}", orderDto);
        OrderDto createdOrder = orderService.createOrder(orderDto);
        log.info("Заказ создан: {}", createdOrder);
        return createdOrder;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Обновить заказ", description = "Обновляет существующий заказ")
    public OrderDto updateOrder(@PathVariable @Min(1) Long id,
                                @RequestBody @Valid OrderDto orderDto) {
        log.info("Получен запрос на обновление заказа: {}", orderDto);
        OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
        log.info("Заказ обновлен: {}", updatedOrder);
        return updatedOrder;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Удалить заказ", description = "Удаляет заказ по его идентификатору")
    public void deleteOrder(@PathVariable @Min(1) Long id) {
        log.info("Получен запрос на удаление заказа с ID {}", id);
        orderService.deleteOrderById(id);
        log.info("Заказ с ID {} удален", id);
    }
}
