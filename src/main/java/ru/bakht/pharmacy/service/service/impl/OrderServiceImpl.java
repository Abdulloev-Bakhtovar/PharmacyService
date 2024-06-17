package ru.bakht.pharmacy.service.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.OrderMapper;
import ru.bakht.pharmacy.service.model.Order;
import ru.bakht.pharmacy.service.model.PharmacyMedication;
import ru.bakht.pharmacy.service.model.PharmacyMedicationId;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.repository.*;
import ru.bakht.pharmacy.service.service.OrderService;

import java.time.LocalDate;
import java.util.List;

/**
 * Реализация интерфейса {@link OrderService} для управления заказами.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PharmacyRepository pharmacyRepository;
    private final MedicationRepository medicationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        log.info("Получение всех заказов");
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        log.info("Получение заказа с идентификатором {}", id);
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Заказ с идентификатором {} не найден", id);
                    return new EntityNotFoundException("Заказ", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Создание нового заказа: {}", orderDto);

        var order = orderMapper.toEntity(orderDto);
        validateAndSetRelatedEntities(order, orderDto);

        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(orderDto.getQuantity() * order.getMedication().getPrice());

        order = orderRepository.save(order);

        updatePharmacyMedicationQuantity(orderDto);

        return orderMapper.toDto(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        log.info("Обновление заказа с идентификатором {}: {}", id, orderDto);

        var existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Заказ с идентификатором {} не найден", id);
                    return new EntityNotFoundException("Заказ", id);
                });

        updateOrderFromDto(existingOrder, orderDto);
        validateAndSetRelatedEntities(existingOrder, orderDto);

        updatePharmacyMedicationQuantity(orderDto);

        return orderMapper.toDto(orderRepository.save(existingOrder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOrderById(Long id) {
        log.info("Удаление заказа с идентификатором {}", id);
        orderRepository.deleteById(id);
    }

    /**
     * Обновляет информацию о заказе на основе данных из DTO.
     *
     * @param order объект Order, который необходимо обновить
     * @param orderDto объект OrderDto с новыми данными
     */
    private void updateOrderFromDto(Order order, OrderDto orderDto) {
        order.setQuantity(orderDto.getQuantity());
        order.setStatus(orderDto.getStatus());
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(orderDto.getQuantity() * order.getMedication().getPrice());
    }

    /**
     * Проверяет наличие связанных сущностей по их идентификаторам в DTO.
     * и устанавливает связанные сущности в объекте Order на основе данных из DTO.
     *
     * @param order объект Order, который необходимо обновить
     * @param orderDto объект OrderDto с новыми данными
     */
    private void validateAndSetRelatedEntities(Order order, OrderDto orderDto) {
        Long employeeId = orderDto.getEmployeeDto().getId();
        Long customerId = orderDto.getCustomerDto().getId();
        Long pharmacyId = orderDto.getPharmacyDto().getId();
        Long medicationId = orderDto.getMedicationDto().getId();

        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник", employeeId));
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Покупатель", customerId));
        var pharmacy  = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new EntityNotFoundException("Аптека", pharmacyId));
        var medication = medicationRepository.findById(medicationId)
                .orElseThrow(() ->new EntityNotFoundException("Лекарство", medicationId));
        var pharmacyMedication = entityManager.find(PharmacyMedication.class, new PharmacyMedicationId(pharmacyId, medicationId));
        if (pharmacyMedication == null) {
            throw new EntityNotFoundException("Связь между аптекой и лекарством", pharmacyId, medicationId);
        }
        if (pharmacyMedication.getQuantity() < orderDto.getQuantity()) {
            throw new IllegalArgumentException("Количество лекарства в заказе " + orderDto.getQuantity()
                    + " превышает количество на складе " + pharmacyMedication.getQuantity());
        }
        if (!employee.getPharmacy().getId().equals(pharmacyId)) {
            throw new EntityNotFoundException("Сотрудник не работает в указанной аптеке", employeeId);
        }

        order.setEmployee(employee);
        order.setCustomer(customer);
        order.setPharmacy(pharmacy);
        order.setMedication(medication);
    }

    /**
     * Обновляет количество лекарства в аптеке после создания или обновления заказа.
     *
     * @param orderDto объект OrderDto
     */
    private void updatePharmacyMedicationQuantity(OrderDto orderDto) {
        Long pharmacyId = orderDto.getPharmacyDto().getId();
        Long medicationId = orderDto.getMedicationDto().getId();
        var pharmacyMedication = entityManager.find(PharmacyMedication.class, new PharmacyMedicationId(pharmacyId, medicationId));
        if (pharmacyMedication == null) {
            throw new EntityNotFoundException("Связь между аптекой и лекарством", pharmacyId, medicationId);
        }

        int remainingQuantity = pharmacyMedication.getQuantity() - orderDto.getQuantity();
        pharmacyMedication.setQuantity(remainingQuantity);
        entityManager.merge(pharmacyMedication);
    }
}
