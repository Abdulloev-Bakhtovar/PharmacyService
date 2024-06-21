package ru.bakht.pharmacy.service.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bakht.pharmacy.service.enums.FileFormat;

@Service
@RequiredArgsConstructor
public class ReportFactory {

    private final ExcelReportService excelReportService;
    private final PdfReportService pdfReportService;

    public ReportGenerator getReportGenerator(FileFormat fileFormat) {
        return switch (fileFormat) {
            case EXCEL -> excelReportService;
            case PDF -> pdfReportService;
        };
    }
}
