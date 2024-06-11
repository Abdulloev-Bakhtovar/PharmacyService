package ru.bakht.pharmacy.service.service;

/**
 * Интерфейс для записи запросов на отчеты.
 */
public interface ReportRequestService {

    /**
     * Записывает запрос на отчет с заданным именем отчета.
     *
     * @param reportName имя отчета
     */
    void recordReportRequest(String reportName);
}
