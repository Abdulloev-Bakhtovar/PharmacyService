package ru.bakht.pharmacy.service.service;

import ru.bakht.pharmacy.service.enums.ReportType;

/**
 * Интерфейс для записи запросов на отчеты.
 */
public interface ReportRequestService {

    /**
     * Записывает запрос на отчет с заданным именем отчета.
     *
     * @param reportName имя отчета
     */
    void recordReportRequest(ReportType reportName);
}
