package ru.bakht.pharmacy.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bakht.pharmacy.service.enums.Format;
import ru.bakht.pharmacy.service.service.impl.ExcelReportService;
import ru.bakht.pharmacy.service.service.impl.PdfReportService;

@Service
@RequiredArgsConstructor
public class ReportFactory {

    private final ExcelReportService excelReportService;
    private final PdfReportService pdfReportService;

    public ReportGenerator getReportGenerator(Format format) {
        return switch (format) {
            case EXCEL -> excelReportService;
            case PDF -> pdfReportService;
        };
    }
}
