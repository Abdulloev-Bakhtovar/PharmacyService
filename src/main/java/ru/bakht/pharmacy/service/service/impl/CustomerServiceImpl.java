package ru.bakht.pharmacy.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.CustomerMapper;
import ru.bakht.pharmacy.service.model.Customer;
import ru.bakht.pharmacy.service.model.dto.CustomerDto;
import ru.bakht.pharmacy.service.repository.CustomerRepository;
import ru.bakht.pharmacy.service.service.CustomerService;

import java.util.List;

/**
 * Реализация интерфейса CustomerService.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        log.info("Fetching customer with id {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Customer with id {} not found", id);
                    return new EntityNotFoundException("Клиент", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        var id = customerDto.getId();

        if (id != null && customerRepository.existsById(id)) {
            log.info("Customer with id {} already exists, updating customer", id);
            return updateCustomer(id, customerDto);
        }

        log.info("Creating new customer: {}", customerDto);
        var customer = customerRepository.save(customerMapper.toEntity(customerDto));
        log.info("Created customer: {}", customer);
        return customerMapper.toDto(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        log.info("Updating customer: {}", customerDto);
        var existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer with id {} not found", id);
                    return new EntityNotFoundException("Клиент", id);
                });

        updateCustomerFromDto(existingCustomer, customerDto);
        return customerMapper.toDto(customerRepository.save(existingCustomer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCustomerById(Long id) {
        log.info("Deleting customer with id {}", id);
        customerRepository.findById(id).ifPresentOrElse(
                customer -> {
                    customerRepository.deleteById(id);
                    log.info("Deleted customer with id {}", id);
                },
                () -> {
                    log.error("Customer with id {} not found", id);
                    throw new EntityNotFoundException("Клиент", id);
                }
        );
    }

    /**
     * Обновляет информацию о клиенте на основе данных из DTO.
     *
     * @param customer объект Customer, который необходимо обновить
     * @param customerDto объект CustomerDto с новыми данными
     */
    private void updateCustomerFromDto(Customer customer, CustomerDto customerDto) {
        customer.setName(customerDto.getName());
        customer.setAddress(customerDto.getAddress());
        customer.setPhone(customerDto.getPhone());
    }
}
