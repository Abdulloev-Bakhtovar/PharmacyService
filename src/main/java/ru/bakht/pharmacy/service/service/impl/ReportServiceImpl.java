package ru.bakht.pharmacy.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.mapper.MedicationMapper;
import ru.bakht.pharmacy.service.mapper.OrderMapper;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;
import ru.bakht.pharmacy.service.model.dto.TotalOrdersProjection;
import ru.bakht.pharmacy.service.repository.MedicationRepository;
import ru.bakht.pharmacy.service.repository.OrderRepository;
import ru.bakht.pharmacy.service.service.ReportService;

import java.util.Date;
import java.util.List;

/**
 * Реализация интерфейса {@link ReportService} для генерации отчетов, связанных с медикаментами и заказами.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final MedicationRepository medicationRepository;
    private final OrderRepository orderRepository;
    private final MedicationMapper medicationMapper;
    private final OrderMapper orderMapper;

    /**
     * {@inheritDoc}
     */
    public List<MedicationDto> getMedicationsByPharmacy(Long pharmacyId) {
        log.info("Received request to get medications for pharmacy with id {}", pharmacyId);
        List<MedicationDto> medications = mapToDto(
                medicationRepository.findMedicationsByPharmacyId(pharmacyId), medicationMapper::toDto);
        log.info("Returning {} medications for pharmacy with id {}", medications.size(), pharmacyId);
        return medications;
    }

    /**
     * {@inheritDoc}
     */
    public TotalOrders getTotalQuantityAndAmount(Date startDate, Date endDate) {
        log.info("Received request to get total quantity and amount of orders from {} to {}", startDate, endDate);
        TotalOrdersProjection projection = orderRepository.findTotalQuantityAndAmountByDateRange(startDate, endDate);
        Integer totalQuantity = (projection.getTotalQuantity() != null) ? projection.getTotalQuantity() : 0;
        Double totalAmount = (projection.getTotalAmount() != null) ? projection.getTotalAmount() : 0.0;
        TotalOrders totalOrders = new TotalOrders(totalQuantity, totalAmount);
        log.info("Returning total quantity: {}, total amount: {}",
                totalOrders.getTotalQuantity(), totalOrders.getTotalAmount());
        return totalOrders;
    }

    /**
     * {@inheritDoc}
     */
    public List<OrderDto> getOrdersByCustomerPhone(String phone) {
        log.info("Received request to get orders for customer with phone {}", phone);
        List<OrderDto> orders = mapToDto(orderRepository.findOrdersByCustomerPhone(phone), orderMapper::toDto);
        log.info("Returning {} orders for customer with phone {}", orders.size(), phone);
        return orders;
    }

    /**
     * {@inheritDoc}
     */
    public List<MedicationDto> getOutOfStockMedicationsByPharmacy(Long pharmacyId) {
        log.info("Received request to get out of stock medications for pharmacy with id {}", pharmacyId);
        List<MedicationDto> medications = mapToDto(
                medicationRepository.findOutOfStockMedicationsByPharmacyId(pharmacyId), medicationMapper::toDto);
        log.info("Returning {} out of stock medications for pharmacy with id {}", medications.size(), pharmacyId);
        return medications;
    }

    /**
     * Универсальный метод для маппинга сущностей в DTO.
     *
     * @param <T> тип сущности
     * @param <R> тип DTO
     * @param entities список сущностей
     * @param mapper функция для маппинга сущности в DTO
     * @return список DTO
     */
    private <T, R> List<R> mapToDto(List<T> entities, java.util.function.Function<T, R> mapper) {
        return entities.stream().map(mapper).toList();
    }
}
