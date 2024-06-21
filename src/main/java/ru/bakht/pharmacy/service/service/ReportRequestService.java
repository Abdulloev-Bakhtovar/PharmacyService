package ru.bakht.pharmacy.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.enums.ReportType;
import ru.bakht.pharmacy.service.model.ReportRequest;
import ru.bakht.pharmacy.service.repository.ReportRequestRepository;

import java.time.LocalDate;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReportRequestService {

    private final ReportRequestRepository reportRequestRepository;

    /**
     * Записывает запрос на отчет с заданным именем отчета.
     *
     * @param reportName имя отчета
     */
    public void recordReportRequest(ReportType reportName) {
        log.info("Запись запроса на отчет: {}", reportName);

        ReportRequest reportRequest = reportRequestRepository.findByReportName(reportName)
                .orElse(ReportRequest.builder()
                        .reportName(reportName)
                        .requestCount(0)
                        .lastRequestTime(LocalDate.now())
                        .build());

        reportRequest.setRequestCount(reportRequest.getRequestCount() + 1);
        reportRequest.setLastRequestTime(LocalDate.now());

        reportRequestRepository.save(reportRequest);

        log.info("Запрос на отчет {} успешно зарегистрирован", reportName);
    }

}
