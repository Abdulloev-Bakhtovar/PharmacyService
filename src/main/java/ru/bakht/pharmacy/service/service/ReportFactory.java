package ru.bakht.pharmacy.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bakht.pharmacy.service.service.impl.ExcelReportService;
import ru.bakht.pharmacy.service.service.impl.PdfReportService;

@Service
@RequiredArgsConstructor
public class ReportFactory {

    private final ExcelReportService excelReportService;
    private final PdfReportService pdfReportService;

    public ReportGenerator getReportGenerator(String format) {
        return switch (format.toLowerCase()) {
            case "excel" -> excelReportService;
            case "pdf" -> pdfReportService;
            default -> throw new IllegalArgumentException("Неизвестный формат отчета: " + format);
        };
    }
}

