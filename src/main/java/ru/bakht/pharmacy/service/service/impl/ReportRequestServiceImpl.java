package ru.bakht.pharmacy.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.model.ReportRequest;
import ru.bakht.pharmacy.service.repository.ReportRequestRepository;
import ru.bakht.pharmacy.service.service.ReportRequestService;

import java.util.Date;

/**
 * Реализация интерфейса {@link ReportRequestService} для записи запросов на отчеты.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportRequestServiceImpl implements ReportRequestService {

    private final ReportRequestRepository reportRequestRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void recordReportRequest(String reportName) {
        log.info("Recording report request for report: {}", reportName);

        ReportRequest reportRequest = reportRequestRepository.findByReportName(reportName)
                .orElse(ReportRequest.builder()
                        .reportName(reportName)
                        .requestCount(0)
                        .lastRequestTime(new Date())
                        .build());

        reportRequest.setRequestCount(reportRequest.getRequestCount() + 1);
        reportRequest.setLastRequestTime(new Date());

        reportRequestRepository.save(reportRequest);

        log.info("Report request for {} recorded successfully", reportName);
    }
}
