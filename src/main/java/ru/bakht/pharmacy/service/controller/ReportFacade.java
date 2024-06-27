package ru.bakht.pharmacy.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bakht.pharmacy.service.enums.FileFormat;
import ru.bakht.pharmacy.service.enums.ReportType;
import ru.bakht.pharmacy.service.feign.ReportServiceClient;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;
import ru.bakht.pharmacy.service.service.report.ReportFactory;
import ru.bakht.pharmacy.service.service.report.ReportGenerator;
import ru.bakht.pharmacy.service.service.report.ReportService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportFacade {

    private final ReportFactory reportFactory;
    private final ReportService reportService;
    private final ReportServiceClient reportServiceClient;

    public List<MedicationDto> getMedicationsByPharmacy(Long pharmacyId) {
        reportServiceClient.recordReportRequest(ReportType.MEDICATIONS);
        return reportService.getMedicationsByPharmacy(pharmacyId);
    }

    public TotalOrders getTotalQuantityAndAmount(LocalDate startDate, LocalDate endDate) {
        reportServiceClient.recordReportRequest(ReportType.TOTAL_ORDERS);
        return reportService.getTotalQuantityAndAmount(startDate, endDate);
    }

    public List<OrderDto> getOrdersByCustomerPhone(String phone) {
        reportServiceClient.recordReportRequest(ReportType.CUSTOMER_ORDERS);
        return reportService.getOrdersByCustomerPhone(phone);
    }

    public List<MedicationDto> getOutOfStockMedicationsByPharmacy(Long pharmacyId) {
        reportServiceClient.recordReportRequest(ReportType.OUT_OF_STOCK_MEDICATIONS);
        return reportService.getOutOfStockMedicationsByPharmacy(pharmacyId);
    }

    public byte[] exportMedicationsByPharmacy(Long pharmacyId, FileFormat fileFormat) throws IOException {
        List<MedicationDto> medications = reportService.getMedicationsByPharmacy(pharmacyId);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(fileFormat);
        return reportGenerator.generateMedicationsReport(medications);
    }

    public byte[] exportTotalQuantityAndAmount(
            LocalDate startDate, LocalDate endDate, FileFormat fileFormat) throws IOException {
        TotalOrders totalOrders = reportService.getTotalQuantityAndAmount(startDate, endDate);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(fileFormat);
        return reportGenerator.generateTotalOrdersReport(totalOrders);
    }

    public byte[] exportOrdersByCustomerPhone(String phone, FileFormat fileFormat) throws IOException {
        List<OrderDto> orders = reportService.getOrdersByCustomerPhone(phone);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(fileFormat);
        return reportGenerator.generateOrdersReport(orders);
    }

    public byte[] exportOutOfStockMedicationsByPharmacy(Long pharmacyId, FileFormat fileFormat) throws IOException {
        List<MedicationDto> medications = reportService.getOutOfStockMedicationsByPharmacy(pharmacyId);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(fileFormat);
        return reportGenerator.generateMedicationsReport(medications);
    }
}
