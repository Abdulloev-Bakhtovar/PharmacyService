package ru.bakht.pharmacy.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bakht.pharmacy.service.enums.EmployeePosition;
import ru.bakht.pharmacy.service.enums.MedicationForm;
import ru.bakht.pharmacy.service.enums.OrderStatus;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.OrderMapper;
import ru.bakht.pharmacy.service.model.*;
import ru.bakht.pharmacy.service.model.dto.*;
import ru.bakht.pharmacy.service.repository.*;
import ru.bakht.pharmacy.service.service.OrderService;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

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
    private OrderService orderService;

    private Order order;
    private OrderDto orderDto;
    private Employee employee;
    private Customer customer;
    private Pharmacy pharmacy;
    private Medication medication;
    private PharmacyMedication pharmacyMedication;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        pharmacy = new Pharmacy(
                1L, "Аптека №1", "ул. Ленина, 2", "89007654321", null);
        employee = new Employee(
                1L, "Алексей Смирнов", EmployeePosition.PHARMACIST, "alexey@example.com", pharmacy);
        customer = new Customer(
                1L, "Мария Иванова", "ул. Ленина, 1", "89001234567");
        medication = new Medication(
                1L, "Аспирин", MedicationForm.TABLET, 100.0, null);
        pharmacyMedication = new PharmacyMedication(new PharmacyMedicationId(
                1L, 1L), pharmacy, medication, 50);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setEmployee(new EmployeeDto(
                1L, "Алексей Смирнов", EmployeePosition.PHARMACIST, "alexey@example.com", null));
        orderDto.setCustomer(new CustomerDto(
                1L, "Мария Иванова", "ул. Ленина, 1", "89001234567"));
        orderDto.setPharmacy(new PharmacyDto(
                1L, "Аптека №1", "ул. Ленина, 2", "89007654321"));
        orderDto.setMedication(new MedicationDto(
                1L, "Аспирин", MedicationForm.TABLET, 100.0, null));
        orderDto.setQuantity(2);
        orderDto.setOrderStatus(OrderStatus.NEW);

        order = new Order();
        order.setId(1L);
        order.setEmployee(employee);
        order.setCustomer(customer);
        order.setPharmacy(pharmacy);
        order.setMedication(medication);
        order.setQuantity(2);
        order.setOrderStatus(OrderStatus.NEW);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(200.0);

        Field entityManagerField = OrderService.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(orderService, entityManager);
    }

    @Test
    void getAllOrders_ReturnsOrderList() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        List<OrderDto> result = orderService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDto, result.getFirst());
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toDto(any(Order.class));
    }

    @Test
    void getOrderById_ReturnsOrderDto() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        OrderDto result = orderService.getById(1L);

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
                () -> orderService.getById(1L)
        );

        assertTrue(thrown.getMessage().contains("Заказ с ID 1 не найден"));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void createOrder_ReturnsOrderDto() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(entityManager.find(eq(PharmacyMedication.class),
                any(PharmacyMedicationId.class))).thenReturn(pharmacyMedication);
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        OrderDto result = orderService.create(orderDto);

        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(entityManager, times(1)).merge(any(PharmacyMedication.class));
    }

    @Test
    void updateOrder_ReturnsOrderDto() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(entityManager.find(eq(PharmacyMedication.class),
                any(PharmacyMedicationId.class))).thenReturn(pharmacyMedication);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        OrderDto result = orderService.update(1L, orderDto);

        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(entityManager, times(1)).merge(any(PharmacyMedication.class));
    }


    @Test
    void deleteOrderById_SuccessfulDeletion() {
        Long orderId = 10L;

        orderService.delete(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

}
