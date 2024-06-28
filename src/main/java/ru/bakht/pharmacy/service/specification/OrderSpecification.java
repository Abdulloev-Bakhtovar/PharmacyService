package ru.bakht.pharmacy.service.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.bakht.pharmacy.service.enums.OrderStatus;
import ru.bakht.pharmacy.service.model.Order;

import java.time.LocalDate;

public class OrderSpecification {

    public static Specification<Order> hasCustomerId(Long customerId) {
        return (root, query, cb) -> customerId == null ? null : cb.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<Order> hasEmployeeId(Long employeeId) {
        return (root, query, cb) -> employeeId == null ? null : cb.equal(root.get("employee").get("id"), employeeId);
    }

    public static Specification<Order> hasPharmacyId(Long pharmacyId) {
        return (root, query, cb) -> pharmacyId == null ? null : cb.equal(root.get("pharmacy").get("id"), pharmacyId);
    }

    public static Specification<Order> hasMedicationId(Long medicationId) {
        return (root, query, cb) -> medicationId == null ? null : cb.equal(root.get("medication").get("id"), medicationId);
    }

    public static Specification<Order> hasOrderStatus(OrderStatus orderStatus) {
        return (root, query, cb) -> orderStatus == null ? null : cb.equal(root.get("orderStatus"), orderStatus);
    }

    public static Specification<Order> hasOrderDate(LocalDate orderDate) {
        return (root, query, cb) -> orderDate == null ? null : cb.equal(root.get("orderDate"), orderDate);
    }
}
