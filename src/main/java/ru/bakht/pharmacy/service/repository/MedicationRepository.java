package ru.bakht.pharmacy.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bakht.pharmacy.service.model.Medication;
import ru.bakht.pharmacy.service.model.PharmacyMedication;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long>, JpaSpecificationExecutor<Medication> {

    @Query(value = "SELECT *"
            + "FROM medications m "
            + "JOIN pharmacy_medications pm ON pm.medication_id = m.id "
            + "WHERE pm.pharmacy_id = :pharmacyId", nativeQuery = true)
    List<Medication> findMedicationsByPharmacyId(@Param("pharmacyId") Long pharmacyId);

    @Query(value = "SELECT * "
            + "FROM pharmacy_medications pm "
            + "JOIN medications m ON pm.medication_id = m.id "
            + "WHERE pm.quantity = 0 AND pm.pharmacy_id = :pharmacyId", nativeQuery = true)
    List<Medication> findOutOfStockMedicationsByPharmacyId(@Param("pharmacyId") Long pharmacyId);

    @Query(value = "SELECT pm "
            + "FROM PharmacyMedication pm "
            + "WHERE pm.quantity < :threshold")
    List<PharmacyMedication> findMedicationsBelowThreshold(@Param("threshold") Integer threshold);
}
