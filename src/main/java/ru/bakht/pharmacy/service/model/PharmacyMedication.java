package ru.bakht.pharmacy.service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"pharmacy","medication"})
@Builder
@Entity
@Table(name = "pharmacy_medications")
public class PharmacyMedication {

    @EmbeddedId
    PharmacyMedicationId id;

    @ManyToOne
    @MapsId("pharmacyId")
    @JoinColumn(name = "pharmacy_id")
    Pharmacy pharmacy;

    @ManyToOne
    @MapsId("medicationId")
    @JoinColumn(name = "medication_id")
    Medication medication;

    @Column(name = "quantity")
    Integer quantity;
}
