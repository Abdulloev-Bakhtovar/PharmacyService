package ru.bakht.pharmacy.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PharmacyMedicationId implements Serializable {

    @Column(name = "pharmacy_id")
    Long pharmacyId;

    @Column(name = "medication_id")
    Long medicationId;
}
