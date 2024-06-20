package ru.bakht.pharmacy.service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bakht.pharmacy.service.enums.Position;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"pharmacy"})
@EqualsAndHashCode(exclude = {"pharmacy"})
@Builder
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name")
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    Position position;

    @Column(name = "email")
    String email;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    Pharmacy pharmacy;
}
