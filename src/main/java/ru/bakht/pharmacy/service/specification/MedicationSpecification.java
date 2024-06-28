package ru.bakht.pharmacy.service.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.bakht.pharmacy.service.enums.MedicationForm;
import ru.bakht.pharmacy.service.model.Medication;

import java.time.LocalDate;

public class MedicationSpecification {

    public static Specification<Medication> hasName(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(
                builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Medication> hasForm(MedicationForm form) {
        return (root, query, builder) -> form == null ? null : builder.equal(root.get("form"), form);
    }

    public static Specification<Medication> hasPrice(Double price) {
        return (root, query, builder) -> price == null ? null : builder.equal(root.get("price"), price);
    }

    public static Specification<Medication> hasExpirationDate(LocalDate expirationDate) {
        return (root, query, builder) -> expirationDate == null ? null : builder.equal(
                root.get("expirationDate"), expirationDate);
    }
}
