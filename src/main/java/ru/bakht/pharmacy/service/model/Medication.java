package ru.bakht.pharmacy.service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bakht.pharmacy.service.model.enums.Form;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name")
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "form")
    Form form;

    @Column(name = "price")
    Double price;

    @Column(name = "expiration_date")
    Date expirationDate;
}
