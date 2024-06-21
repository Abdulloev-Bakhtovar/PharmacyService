package ru.bakht.pharmacy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bakht.pharmacy.service.mapper.MedicationMapper;
import ru.bakht.pharmacy.service.mapper.OrderMapper;
import ru.bakht.pharmacy.service.model.Medication;
import ru.bakht.pharmacy.service.model.Order;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;
import ru.bakht.pharmacy.service.model.dto.TotalOrdersProjection;
import ru.bakht.pharmacy.service.repository.MedicationRepository;
import ru.bakht.pharmacy.service.repository.OrderRepository;
import ru.bakht.pharmacy.service.service.report.ReportService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MedicationMapper medicationMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private ReportService reportService;

    private Long pharmacyId;
    private String customerPhone;
    private LocalDate startDate;
    private LocalDate endDate;
    private TotalOrdersProjection totalOrdersProjection;

    @BeforeEach
    void setUp() {
        pharmacyId = 1L;
        customerPhone = "1234567890";
        startDate = LocalDate.now();
        endDate = LocalDate.now();
        totalOrdersProjection = new TotalOrdersProjection() {
            @Override
            public Integer getTotalQuantity() {
                return 10;
            }

            @Override
            public Double getTotalAmount() {
                return 200.0;
            }
        };
    }

    @Test
    void getMedicationsByPharmacy() {
        List<MedicationDto> medicationDtos = List.of(new MedicationDto(), new MedicationDto());
        when(medicationRepository.findMedicationsByPharmacyId(pharmacyId)).thenReturn(List.of(new Medication(), new Medication()));
        when(medicationMapper.toDto(any(Medication.class))).thenReturn(new MedicationDto());

        List<MedicationDto> result = reportService.getMedicationsByPharmacy(pharmacyId);

        assertEquals(2, result.size());
        verify(medicationRepository).findMedicationsByPharmacyId(pharmacyId);
        verify(medicationMapper, times(2)).toDto(any(Medication.class));
    }

    @Test
    void getTotalQuantityAndAmount() {
        when(orderRepository.findTotalQuantityAndAmountByDateRange(startDate, endDate)).thenReturn(totalOrdersProjection);

        TotalOrders result = reportService.getTotalQuantityAndAmount(startDate, endDate);

        assertEquals(10, result.getTotalQuantity());
        assertEquals(200.0, result.getTotalAmount());
        verify(orderRepository).findTotalQuantityAndAmountByDateRange(startDate, endDate);
    }

    @Test
    void getOrdersByCustomerPhone() {
        List<OrderDto> orderDtos = List.of(new OrderDto(), new OrderDto());
        when(orderRepository.findOrdersByCustomerPhone(customerPhone)).thenReturn(List.of(new Order(), new Order()));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        List<OrderDto> result = reportService.getOrdersByCustomerPhone(customerPhone);

        assertEquals(2, result.size());
        verify(orderRepository).findOrdersByCustomerPhone(customerPhone);
        verify(orderMapper, times(2)).toDto(any(Order.class));
    }

    @Test
    void getOutOfStockMedicationsByPharmacy() {
        List<MedicationDto> medicationDtos = List.of(new MedicationDto(), new MedicationDto());
        when(medicationRepository.findOutOfStockMedicationsByPharmacyId(pharmacyId)).thenReturn(List.of(new Medication(), new Medication()));
        when(medicationMapper.toDto(any(Medication.class))).thenReturn(new MedicationDto());

        List<MedicationDto> result = reportService.getOutOfStockMedicationsByPharmacy(pharmacyId);

        assertEquals(2, result.size());
        verify(medicationRepository).findOutOfStockMedicationsByPharmacyId(pharmacyId);
        verify(medicationMapper, times(2)).toDto(any(Medication.class));
    }
}
