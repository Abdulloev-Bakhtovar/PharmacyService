package ru.bakht.pharmacy.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;
import ru.bakht.pharmacy.service.service.ReportFactory;
import ru.bakht.pharmacy.service.service.ReportGenerator;
import ru.bakht.pharmacy.service.service.ReportRequestService;
import ru.bakht.pharmacy.service.service.ReportService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с отчетами о медикаментах и заказах.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportFactory reportFactory;
    private final ReportService reportService;
    private final ReportRequestService reportRequestService;

    @GetMapping("/medications/pharmacy/{pharmacyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить медикаменты по ID аптеки",
            description = "Возвращает список медикаментов, доступных в конкретной аптеке")
    public List<MedicationDto> getMedicationsByPharmacy(@PathVariable Long pharmacyId) {
        log.info("Получен запрос на получение медикаментов для аптеки с id {}", pharmacyId);
        reportRequestService.recordReportRequest("Medications Report");
        List<MedicationDto> medications = reportService.getMedicationsByPharmacy(pharmacyId);
        log.info("Возвращено {} медикаментов для аптеки с id {}", medications.size(), pharmacyId);
        return medications;
    }

    @GetMapping("/total-quantity-and-amount")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить общее количество и общую стоимость заказов",
            description = "Возвращает общее количество и общую стоимость всех заказов за указанный период")
    public TotalOrders getTotalQuantityAndAmount(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        log.info("Получен запрос на получение общего количества и стоимости заказов с {} по {}", startDate, endDate);
        reportRequestService.recordReportRequest("Total Orders Report");
        TotalOrders totalOrders = reportService.getTotalQuantityAndAmount(startDate, endDate);
        log.info("Возвращено общее количество: {}, общая стоимость: {}", totalOrders.getTotalQuantity(), totalOrders.getTotalAmount());
        return totalOrders;
    }

    @GetMapping("/orders/customer")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить заказы по телефону клиента",
            description = "Возвращает список заказов, сделанных конкретным клиентом по его номеру телефона")
    public List<OrderDto> getOrdersByCustomerPhone(@RequestParam String phone) {
        log.info("Получен запрос на получение заказов для клиента с телефоном {}", phone);
        reportRequestService.recordReportRequest("Customer Orders Report");
        List<OrderDto> orders = reportService.getOrdersByCustomerPhone(phone);
        log.info("Возвращено {} заказов для клиента с телефоном {}", orders.size(), phone);
        return orders;
    }

    @GetMapping("/out-of-stock-medications/pharmacy/{pharmacyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить медикаменты, закончившиеся на складе в определенной аптеке",
            description = "Возвращает список медикаментов, которые закончились на складе в определенной аптеке")
    public List<MedicationDto> getOutOfStockMedicationsByPharmacy(@PathVariable Long pharmacyId) {
        log.info("Получен запрос на получение медикаментов, закончившихся на складе для аптеки с id {}", pharmacyId);
        reportRequestService.recordReportRequest("Out of Stock Medications Report");
        List<MedicationDto> medications = reportService.getOutOfStockMedicationsByPharmacy(pharmacyId);
        log.info("Возвращено {} медикаментов, закончившихся на складе для аптеки с id {}", medications.size(), pharmacyId);
        return medications;
    }

    @GetMapping("/export/medications/pharmacy/{pharmacyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Экспортировать медикаменты по ID аптеки",
            description = "Экспортирует список медикаментов, доступных в конкретной аптеке")
    public ResponseEntity<byte[]> exportMedicationsByPharmacy(@PathVariable Long pharmacyId,
                                                              @RequestParam String format) throws IOException {
        log.info("Получен запрос на экспорт медикаментов для аптеки с id {} в формате {}", pharmacyId, format);
        List<MedicationDto> medications = reportService.getMedicationsByPharmacy(pharmacyId);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(format);
        byte[] reportData = reportGenerator.generateMedicationsReport(medications);
        reportRequestService.recordReportRequest("Medications Export Report");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=medications." + format);
        return ResponseEntity.ok().headers(headers).body(reportData);
    }

    @GetMapping("/export/total-quantity-and-amount")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Экспортировать общее количество и общую стоимость заказов",
            description = "Экспортирует общее количество и общую стоимость всех заказов за указанный период")
    public ResponseEntity<byte[]> exportTotalQuantityAndAmount(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                               @RequestParam String format) throws IOException {
        log.info("Получен запрос на экспорт общего количества и стоимости заказов с {} по {} в формате {}", startDate, endDate, format);
        TotalOrders totalOrders = reportService.getTotalQuantityAndAmount(startDate, endDate);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(format);
        byte[] reportData = reportGenerator.generateTotalOrdersReport(totalOrders);
        reportRequestService.recordReportRequest("Total Orders Export Report");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=total_orders." + format);
        return ResponseEntity.ok().headers(headers).body(reportData);
    }

    @GetMapping("/export/orders/customer")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Экспортировать заказы по телефону клиента",
            description = "Экспортирует список заказов, сделанных конкретным клиентом по его номеру телефона")
    public ResponseEntity<byte[]> exportOrdersByCustomerPhone(@RequestParam String phone,
                                                              @RequestParam String format) throws IOException {
        log.info("Получен запрос на экспорт заказов для клиента с телефоном {} в формате {}", phone, format);
        List<OrderDto> orders = reportService.getOrdersByCustomerPhone(phone);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(format);
        byte[] reportData = reportGenerator.generateOrdersReport(orders);
        reportRequestService.recordReportRequest("Customer Orders Export Report");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customer_orders." + format);
        return ResponseEntity.ok().headers(headers).body(reportData);
    }

    @GetMapping("/export/out-of-stock-medications/pharmacy/{pharmacyId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Экспортировать медикаменты, закончившиеся на складе в определенной аптеке",
            description = "Экспортирует список медикаментов, которые закончились на складе в определенной аптеке")
    public ResponseEntity<byte[]> exportOutOfStockMedicationsByPharmacy(@PathVariable Long pharmacyId,
                                                                        @RequestParam String format) throws IOException {
        log.info("Получен запрос на экспорт медикаментов, закончившихся на складе для аптеки с id {} в формате {}", pharmacyId, format);
        List<MedicationDto> medications = reportService.getOutOfStockMedicationsByPharmacy(pharmacyId);
        ReportGenerator reportGenerator = reportFactory.getReportGenerator(format);
        byte[] reportData = reportGenerator.generateMedicationsReport(medications);
        reportRequestService.recordReportRequest("Out of Stock Medications Export Report");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=out_of_stock_medications." + format);
        return ResponseEntity.ok().headers(headers).body(reportData);
    }
}
