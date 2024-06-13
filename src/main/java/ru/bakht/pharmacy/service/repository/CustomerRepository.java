package ru.bakht.pharmacy.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bakht.pharmacy.service.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
