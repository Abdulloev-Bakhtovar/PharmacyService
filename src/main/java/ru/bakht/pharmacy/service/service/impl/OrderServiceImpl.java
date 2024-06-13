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
 * Реализация интерфейса OrderService.
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
        log.info("Fetching all orders");
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
        log.info("Fetching order with id {}", id);
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Order with id {} not found", id);
                    return new EntityNotFoundException("Заказ", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Creating new order: {}", orderDto);

        validateRelatedEntities(orderDto);

        var order = orderMapper.toEntity(orderDto);
        setRelatedEntities(order, orderDto);

        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(orderDto.getQuantity() * order.getMedication().getPrice());

        order = orderRepository.save(order);

        updatePharmacyMedicationQuantity(orderDto);

        log.info("Created order: {}", order);
        return orderMapper.toDto(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        log.info("Updating order with id {}: {}", id, orderDto);

        var existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order with id {} not found", id);
                    return new EntityNotFoundException("Заказ", id);
                });

        validateRelatedEntities(orderDto);
        updateOrderFromDto(existingOrder, orderDto);
        setRelatedEntities(existingOrder, orderDto);

        existingOrder.setOrderDate(LocalDate.now());
        existingOrder.setTotalAmount(orderDto.getQuantity() * existingOrder.getMedication().getPrice());

        updatePharmacyMedicationQuantity(orderDto);

        return orderMapper.toDto(orderRepository.save(existingOrder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOrderById(Long id) {
        log.info("Deleting order with id {}", id);
        orderRepository.findById(id).ifPresentOrElse(
                order -> {
                    orderRepository.deleteById(id);
                    log.info("Deleted order with id {}", id);
                },
                () -> {
                    log.error("Order with id {} not found", id);
                    throw new EntityNotFoundException("Заказ", id);
                }
        );
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
    }

    /**
     * Проверяет наличие связанных сущностей по их идентификаторам в DTO.
     *
     * @param orderDto объект OrderDto
     */
    private void validateRelatedEntities(OrderDto orderDto) {
        Long employeeId = orderDto.getEmployeeDto().getId();
        Long customerId = orderDto.getCustomerDto().getId();
        Long pharmacyId = orderDto.getPharmacyDto().getId();
        Long medicationId = orderDto.getMedicationDto().getId();

        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник", employeeId));
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Покупатель", customerId);
        }
        if (!pharmacyRepository.existsById(pharmacyId)) {
            throw new EntityNotFoundException("Аптека", pharmacyId);
        }
        if (!medicationRepository.existsById(medicationId)) {
            throw new EntityNotFoundException("Лекарство", medicationId);
        }
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
    }

    /**
     * Устанавливает связанные сущности в объекте Order на основе данных из DTO.
     *
     * @param order объект Order, который необходимо обновить
     * @param orderDto объект OrderDto с новыми данными
     */
    private void setRelatedEntities(Order order, OrderDto orderDto) {
        order.setEmployee(employeeRepository.findById(orderDto.getEmployeeDto().getId())
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник", orderDto.getEmployeeDto().getId())));
        order.setCustomer(customerRepository.findById(orderDto.getCustomerDto().getId())
                .orElseThrow(() -> new EntityNotFoundException("Покупатель", orderDto.getCustomerDto().getId())));
        order.setPharmacy(pharmacyRepository.findById(orderDto.getPharmacyDto().getId())
                .orElseThrow(() -> new EntityNotFoundException("Аптека", orderDto.getPharmacyDto().getId())));
        order.setMedication(medicationRepository.findById(orderDto.getMedicationDto().getId())
                .orElseThrow(() -> new EntityNotFoundException("Лекарство", orderDto.getMedicationDto().getId())));
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
