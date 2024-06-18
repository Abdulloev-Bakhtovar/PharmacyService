package ru.bakht.pharmacy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.CustomerMapper;
import ru.bakht.pharmacy.service.model.Customer;
import ru.bakht.pharmacy.service.model.dto.CustomerDto;
import ru.bakht.pharmacy.service.repository.CustomerRepository;
import ru.bakht.pharmacy.service.service.impl.CustomerServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "Bakha", "Perm", "1234567890");
        customerDto = new CustomerDto(1L, "Bakha", "Perm", "1234567890");
    }

    @Test
    void getAllCustomers_ReturnsCustomerList() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

        List<CustomerDto> customers = customerService.getAllCustomers();

        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals("Bakha", customers.getFirst().getName());
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).toDto(any(Customer.class));
    }

    @Test
    void getCustomerById_ReturnsCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

        CustomerDto result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("Bakha", result.getName());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerMapper, times(1)).toDto(any(Customer.class));
    }

    @Test
    void getCustomerById_ThrowsEntityNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> customerService.getCustomerById(1L),
                "Expected getCustomerById to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Клиент с ID 1 не найден"));
        verify(customerRepository, times(1)).findById(1L);
        verifyNoInteractions(customerMapper);
    }

    @Test
    void createCustomer_ReturnsCustomer() {
        when(customerMapper.toEntity(any(CustomerDto.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

        CustomerDto result = customerService.createCustomer(customerDto);

        assertNotNull(result);
        assertEquals("Bakha", result.getName());
        verify(customerMapper, times(1)).toEntity(any(CustomerDto.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).toDto(any(Customer.class));
    }

    @Test
    void updateCustomer_ReturnsUpdatedCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

        CustomerDto result = customerService.updateCustomer(1L, customerDto);

        assertNotNull(result);
        assertEquals("Bakha", result.getName());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).toDto(any(Customer.class));
    }

    @Test
    void updateCustomer_ThrowsEntityNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> customerService.updateCustomer(1L, customerDto),
                "Expected updateCustomer to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Клиент с ID 1 не найден"));
        verify(customerRepository, times(1)).findById(1L);
        verifyNoInteractions(customerMapper);
    }

    @Test
    void deleteCustomerById_SuccessfulDeletion() {
        Long customerId = 1L;

        customerService.deleteCustomerById(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }
}
