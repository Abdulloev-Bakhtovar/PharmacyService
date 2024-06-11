package ru.bakht.pharmacy.service.service;

import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.model.dto.OrderDto;

import java.util.List;

/**
 * Интерфейс для управления заказами.
 */
public interface OrderService {

    /**
     * Возвращает список всех заказов.
     *
     * @return список объектов OrderDto.
     */
    List<OrderDto> getAllOrders();

    /**
     * Ищет заказ по его идентификатору.
     *
     * @param id идентификатор заказа.
     * @return объект OrderDto, если заказ найден.
     * @throws EntityNotFoundException если заказ с указанным идентификатором не найден.
     */
    OrderDto getOrderById(Long id);

    /**
     * Создает новый заказ.
     *
     * @param orderDto данные для создания заказа.
     * @return созданный объект OrderDto.
     */
    OrderDto createOrder(OrderDto orderDto);

    /**
     * Обновляет существующий заказ.
     *
     * @param id идентификатор заказа.
     * @param orderDto данные для обновления заказа.
     * @return обновленный объект OrderDto.
     * @throws EntityNotFoundException если заказ с указанным идентификатором не найден.
     */
    OrderDto updateOrder(Long id, OrderDto orderDto);

    /**
     * Удаляет заказ по его идентификатору.
     *
     * @param id идентификатор заказа.
     * @throws EntityNotFoundException если заказ с указанным идентификатором не найден.
     */
    void deleteOrderById(Long id);
}
