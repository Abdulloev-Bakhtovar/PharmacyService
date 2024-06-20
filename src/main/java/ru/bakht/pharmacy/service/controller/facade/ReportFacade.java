package ru.bakht.pharmacy.service.controller.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bakht.pharmacy.service.enums.Format;
import ru.bakht.pharmacy.service.enums.ReportType;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;
import ru.bakht.pharmacy.service.service.factory.ReportFactory;
import ru.bakht.pharmacy.service.service.ReportGenerator;
import ru.bakht.pharmacy.service.service.ReportRequestService;
import ru.bakht.pharmacy.service.service.ReportService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportFacade {

    private final ReportFactory reportFactory;
    private final ReportService reportService;
    private final ReportRequestService reportRequestService;

    public List<MedicationDto> getMedicationsByPharmacy(Long pharmacyId) {
        reportRequestService.recordReportRequest(ReportType.MEDICATIONS);
        return reportService.getMedicationsByPharmacy(pharmacyId);
    }

    public TotalOrders getTotalQuantityAndAmount(LocalDate startDate, LocalDate endDate) {
        reportRequestService.recordReportRequest(ReportType.TOTAL_ORDERS);
        return reportService.getTotalQuantityAndAmount(startDate, endDate);
    }

    public List<OrderDto> getOrdersByCustomerPhone(String phone) {
        reportRequestService.recordReportRequest(ReportType.CUSTOMER_ORDERS);
        return reportService.getOrdersByCustomerPhone(phone);
    }

    public List<MedicationDto> getOutOfStockMedicationsByPharmacy(Long pharmacyId) {
        reportRequestService.recordReportRequest(ReportType.OUT_OF_STOCK_MEDICATIONS);
        return reportService.getOutOfStockMedicationsByPharmacy(pharmacyId);
    }

    public byte[] exportMedicationsByPharmacy(Long pharmacyId, Format format) throws IOException {
        List<MedicationDto> medications = reportService.getMedicationsByPharmacy(pharmacyId);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(format);
        return reportGenerator.generateMedicationsReport(medications);
    }

    public byte[] exportTotalQuantityAndAmount(LocalDate startDate, LocalDate endDate, Format format) throws IOException {
        TotalOrders totalOrders = reportService.getTotalQuantityAndAmount(startDate, endDate);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(format);
        return reportGenerator.generateTotalOrdersReport(totalOrders);
    }

    public byte[] exportOrdersByCustomerPhone(String phone, Format format) throws IOException {
        List<OrderDto> orders = reportService.getOrdersByCustomerPhone(phone);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(format);
        return reportGenerator.generateOrdersReport(orders);
    }

    public byte[] exportOutOfStockMedicationsByPharmacy(Long pharmacyId, Format format) throws IOException {
        List<MedicationDto> medications = reportService.getOutOfStockMedicationsByPharmacy(pharmacyId);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(format);
        return reportGenerator.generateMedicationsReport(medications);
    }
}
