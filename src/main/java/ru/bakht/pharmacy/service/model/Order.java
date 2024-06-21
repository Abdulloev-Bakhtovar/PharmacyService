package ru.bakht.pharmacy.service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bakht.pharmacy.service.enums.OrderStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"customer", "employee", "pharmacy", "medication"})
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    Pharmacy pharmacy;

    @ManyToOne
    @JoinColumn(name = "medication_id")
    Medication medication;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "total_amount")
    Double totalAmount;

    @Column(name = "order_date")
    LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    OrderStatus orderStatus;
}
