package ru.bakht.pharmacy.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.OrderMapper;
import ru.bakht.pharmacy.service.model.*;
import ru.bakht.pharmacy.service.model.dto.*;
import ru.bakht.pharmacy.service.model.enums.Form;
import ru.bakht.pharmacy.service.model.enums.Position;
import ru.bakht.pharmacy.service.model.enums.Status;
import ru.bakht.pharmacy.service.repository.*;
import ru.bakht.pharmacy.service.service.impl.OrderServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderDto orderDto;
    private Employee employee;
    private Customer customer;
    private Pharmacy pharmacy;
    private Medication medication;
    private PharmacyMedication pharmacyMedication;

    @BeforeEach
    void setUp() {
        pharmacy = new Pharmacy(1L, "Аптека №1", "ул. Ленина, 2", "89007654321", null);
        employee = new Employee(1L, "Алексей Смирнов", Position.PHARMACIST, "alexey@example.com", pharmacy);
        customer = new Customer(1L, "Мария Иванова", "ул. Ленина, 1", "89001234567");
        medication = new Medication(1L, "Аспирин", Form.TABLET, 100.0, null);
        pharmacyMedication = new PharmacyMedication(new PharmacyMedicationId(1L, 1L), pharmacy, medication, 50);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setEmployeeDto(new EmployeeDto(1L, "Алексей Смирнов", Position.PHARMACIST, "alexey@example.com", null));
        orderDto.setCustomerDto(new CustomerDto(1L, "Мария Иванова", "ул. Ленина, 1", "89001234567"));
        orderDto.setPharmacyDto(new PharmacyDto(1L, "Аптека №1", "ул. Ленина, 2", "89007654321"));
        orderDto.setMedicationDto(new MedicationDto(1L, "Аспирин", Form.TABLET, 100.0, null));
        orderDto.setQuantity(2);
        orderDto.setStatus(Status.NEW);

        order = new Order();
        order.setId(1L);
        order.setEmployee(employee);
        order.setCustomer(customer);
        order.setPharmacy(pharmacy);
        order.setMedication(medication);
        order.setQuantity(2);
        order.setStatus(Status.NEW);
        order.setOrderDate(new Date());
        order.setTotalAmount(200.0);
    }

    @Test
    void getAllOrders_ReturnsOrderList() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        List<OrderDto> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDto, result.get(0));
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toDto(any(Order.class));
    }

    @Test
    void getOrderById_ReturnsOrderDto() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        OrderDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderMapper, times(1)).toDto(any(Order.class));
    }

    @Test
    void getOrderById_ThrowsEntityNotFoundException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> orderService.getOrderById(1L)
        );

        assertTrue(thrown.getMessage().contains("Заказ с ID 1 не найден"));
        verify(orderRepository, times(1)).findById(1L);
    }

    //@Test
    void createOrder_ReturnsOrderDto() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(pharmacyRepository.existsById(1L)).thenReturn(true);
        when(medicationRepository.existsById(1L)).thenReturn(true);
        when(entityManager.find(eq(PharmacyMedication.class), any(PharmacyMedicationId.class))).thenReturn(pharmacyMedication);
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        OrderDto result = orderService.createOrder(orderDto);

        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(entityManager, times(1)).merge(any(PharmacyMedication.class));
    }

    //@Test
    void updateOrder_ReturnsOrderDto() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(pharmacyRepository.existsById(1L)).thenReturn(true);
        when(medicationRepository.existsById(1L)).thenReturn(true);
        when(entityManager.find(eq(PharmacyMedication.class), any(PharmacyMedicationId.class))).thenReturn(pharmacyMedication);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        OrderDto result = orderService.updateOrder(1L, orderDto);

        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(entityManager, times(1)).merge(any(PharmacyMedication.class));
    }

    @Test
    void deleteOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrderById(1L);

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrderById_ThrowsEntityNotFoundException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> orderService.deleteOrderById(1L)
        );

        assertTrue(thrown.getMessage().contains("Заказ с ID 1 не найден"));
        verify(orderRepository, times(1)).findById(1L);
    }
}
