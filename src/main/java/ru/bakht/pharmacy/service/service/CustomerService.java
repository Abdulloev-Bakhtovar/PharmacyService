package ru.bakht.pharmacy.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.CustomerMapper;
import ru.bakht.pharmacy.service.model.dto.CustomerDto;
import ru.bakht.pharmacy.service.repository.CustomerRepository;

import java.util.List;

/**
 * Реализация интерфейса {@link BaseService} для управления клиентами.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService implements BaseService<CustomerDto, Long> {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAll() {
        log.info("Получение всех клиентов");
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CustomerDto getById(Long id) {
        log.info("Получение клиента с идентификатором {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Клиент с идентификатором {} не найден", id);
                    return new EntityNotFoundException("Клиент", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerDto create(CustomerDto customerDto) {
        var id = customerDto.getId();

        if (id != null && customerRepository.existsById(id)) {
            log.info("Клиент с идентификатором {} уже существует, обновление клиента", id);
            return update(id, customerDto);
        }

        log.info("Создание нового клиента: {}", customerDto);
        var customer = customerRepository.save(customerMapper.toEntity(customerDto));
        return customerMapper.toDto(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerDto update(Long id, CustomerDto customerDto) {
        log.info("Обновление клиента: {}", customerDto);
        var existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Клиент с идентификатором {} не найден", id);
                    return new EntityNotFoundException("Клиент", id);
                });

        customerMapper.updateEntityFromDto(customerDto, existingCustomer);
        return customerMapper.toDto(customerRepository.save(existingCustomer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        log.info("Удаление клиента с идентификатором {}", id);
        customerRepository.deleteById(id);
    }
}
