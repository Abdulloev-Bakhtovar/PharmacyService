package ru.bakht.pharmacy.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.bakht.pharmacy.service.enums.FileFormat;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportFacade reportFacade;

    @GetMapping("/medications/pharmacy/{pharmacyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить медикаменты по ID аптеки",
            description = "Возвращает список медикаментов, доступных в конкретной аптеке")
    public List<MedicationDto> getMedicationsByPharmacy(@PathVariable Long pharmacyId) {
        return reportFacade.getMedicationsByPharmacy(pharmacyId);
    }

    @GetMapping("/total-quantity-and-amount")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить общее количество и общую стоимость заказов",
            description = "Возвращает общее количество и общую стоимость всех заказов за указанный период")
    public TotalOrders getTotalQuantityAndAmount(@RequestParam LocalDate startDate,
                                                 @RequestParam LocalDate endDate) {
        return reportFacade.getTotalQuantityAndAmount(startDate, endDate);
    }

    @GetMapping("/orders/customer")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить заказы по телефону клиента",
            description = "Возвращает список заказов, сделанных конкретным клиентом по его номеру телефона")
    public List<OrderDto> getOrdersByCustomerPhone(@RequestParam String phone) {
        return reportFacade.getOrdersByCustomerPhone(phone);
    }

    @GetMapping("/out-of-stock-medications/pharmacy/{pharmacyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить медикаменты, закончившиеся на складе в определенной аптеке",
            description = "Возвращает список медикаментов, которые закончились на складе в определенной аптеке")
    public List<MedicationDto> getOutOfStockMedicationsByPharmacy(@PathVariable Long pharmacyId) {
        return reportFacade.getOutOfStockMedicationsByPharmacy(pharmacyId);
    }

    @GetMapping("/export/medications/pharmacy/{pharmacyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Экспортировать медикаменты по ID аптеки",
            description = "Экспортирует список медикаментов, доступных в конкретной аптеке")
    public ResponseEntity<byte[]> exportMedicationsByPharmacy(@PathVariable Long pharmacyId,
                                                              @RequestParam FileFormat fileFormat) throws IOException {
        byte[] reportData = reportFacade.exportMedicationsByPharmacy(pharmacyId, fileFormat);
        return createFileResponse(reportData, "medications", fileFormat);
    }

    @GetMapping("/export/total-quantity-and-amount")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Экспортировать общее количество и общую стоимость заказов",
            description = "Экспортирует общее количество и общую стоимость всех заказов за указанный период")
    public ResponseEntity<byte[]> exportTotalQuantityAndAmount(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam FileFormat fileFormat) throws IOException {
        byte[] reportData = reportFacade.exportTotalQuantityAndAmount(startDate, endDate, fileFormat);
        return createFileResponse(reportData, "total_orders", fileFormat);
    }

    @GetMapping("/export/orders/customer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Экспортировать заказы по телефону клиента",
            description = "Экспортирует список заказов, сделанных конкретным клиентом по его номеру телефона")
    public ResponseEntity<byte[]> exportOrdersByCustomerPhone(@RequestParam String phone,
                                                              @RequestParam FileFormat fileFormat) throws IOException {
        byte[] reportData = reportFacade.exportOrdersByCustomerPhone(phone, fileFormat);
        return createFileResponse(reportData, "customer_orders", fileFormat);
    }

    @GetMapping("/export/out-of-stock-medications/pharmacy/{pharmacyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Экспортировать медикаменты, закончившиеся на складе в определенной аптеке",
            description = "Экспортирует список медикаментов, которые закончились на складе в определенной аптеке")
    public ResponseEntity<byte[]> exportOutOfStockMedicationsByPharmacy(
            @PathVariable Long pharmacyId, @RequestParam FileFormat fileFormat) throws IOException {
        byte[] reportData = reportFacade.exportOutOfStockMedicationsByPharmacy(pharmacyId, fileFormat);
        return createFileResponse(reportData, "out_of_stock_medications", fileFormat);
    }

    private ResponseEntity<byte[]> createFileResponse(byte[] reportData, String fileName, FileFormat fileFormat) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename=" + fileName + fileFormat.getExtension());
        return ResponseEntity.ok().headers(headers).body(reportData);
    }
}
