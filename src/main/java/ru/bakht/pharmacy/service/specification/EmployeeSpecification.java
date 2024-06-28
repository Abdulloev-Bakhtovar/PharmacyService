package ru.bakht.pharmacy.service.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.bakht.pharmacy.service.enums.EmployeePosition;
import ru.bakht.pharmacy.service.model.Employee;

public class EmployeeSpecification {

    public static Specification<Employee> hasName(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(
                builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Employee> hasPosition(EmployeePosition position) {
        return (root, query, builder) -> position == null ? null : builder.equal(root.get("position"), position);
    }

    public static Specification<Employee> hasEmail(String email) {
        return (root, query, builder) -> email == null ? null : builder.like(
                builder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Employee> hasPharmacyId(Long pharmacyId) {
        return (root, query, builder) -> pharmacyId == null ? null : builder.equal(
                root.get("pharmacy").get("id"), pharmacyId);
    }
}
