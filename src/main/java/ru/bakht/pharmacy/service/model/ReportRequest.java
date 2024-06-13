package ru.bakht.pharmacy.service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "report_requests")
public class ReportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "report_name")
    String reportName;

    @Column(name = "request_count")
    Integer requestCount;

    @Column(name = "last_request_time")
    LocalDate lastRequestTime;
}
