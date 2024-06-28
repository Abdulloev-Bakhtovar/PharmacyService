package ru.bakht.pharmacy.service.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.bakht.pharmacy.service.model.Customer;

public class CustomerSpecification {

    public static Specification<Customer> hasName(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(
                builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Customer> hasAddress(String address) {
        return (root, query, builder) -> address == null ? null : builder.like(
                builder.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    public static Specification<Customer> hasPhone(String phone) {
        return (root, query, builder) -> phone == null ? null : builder.equal(root.get("phone"), phone);
    }
}
