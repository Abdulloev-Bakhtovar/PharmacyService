package ru.bakht.pharmacy.service.service;

import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.model.dto.CustomerDto;

import java.util.List;

/**
 * Интерфейс для управления клиентами.
 */
public interface CustomerService {

    /**
     * Возвращает список всех клиентов.
     *
     * @return список объектов CustomerDto.
     */
    List<CustomerDto> getAllCustomers();

    /**
     * Ищет клиента по его идентификатору.
     *
     * @param id идентификатор клиента.
     * @return объект CustomerDto, если клиент найден.
     * @throws EntityNotFoundException если клиент с указанным идентификатором не найден.
     */
    CustomerDto getCustomerById(Long id);

    /**
     * Создает нового клиента.
     *
     * @param customerDto данные для создания клиента.
     * @return созданный объект CustomerDto.
     */
    CustomerDto createCustomer(CustomerDto customerDto);

    /**
     * Обновляет существующего клиента.
     *
     * @param id идентификатор клиента.
     * @param customerDto данные для обновления клиента.
     * @return обновленный объект CustomerDto.
     * @throws EntityNotFoundException если клиент с указанным идентификатором не найден.
     */
    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    /**
     * Удаляет клиента по его идентификатору.
     *
     * @param id идентификатор клиента.
     * @throws EntityNotFoundException если клиент с указанным идентификатором не найден.
     */
    void deleteCustomerById(Long id);
}