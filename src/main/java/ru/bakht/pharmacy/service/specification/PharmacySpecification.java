package ru.bakht.pharmacy.service.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.bakht.pharmacy.service.model.Pharmacy;

public class PharmacySpecification {

    public static Specification<Pharmacy> hasName(String name) {
        return (root, query, builder) -> name == null ? null : builder.like(
                builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Pharmacy> hasAddress(String address) {
        return (root, query, builder) -> address == null ? null : builder.like(
                builder.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    public static Specification<Pharmacy> hasPhone(String phone) {
        return (root, query, builder) -> phone == null ? null : builder.equal(root.get("phone"), phone);
    }
}
