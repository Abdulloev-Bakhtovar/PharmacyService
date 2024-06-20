package ru.bakht.pharmacy.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bakht.pharmacy.service.enums.ReportType;
import ru.bakht.pharmacy.service.model.ReportRequest;

import java.util.Optional;

@Repository
public interface ReportRequestRepository extends JpaRepository<ReportRequest, Long> {
    Optional<ReportRequest> findByReportName(ReportType reportName);
}
