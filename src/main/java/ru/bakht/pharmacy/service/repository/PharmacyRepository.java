package ru.bakht.pharmacy.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bakht.pharmacy.service.model.Pharmacy;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
