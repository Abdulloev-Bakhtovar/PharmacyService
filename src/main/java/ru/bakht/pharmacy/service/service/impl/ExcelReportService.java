package ru.bakht.pharmacy.service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;
import ru.bakht.pharmacy.service.service.ReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Реализация интерфейса {@link ReportGenerator} для генерации Excel-отчетов.
 */
@Slf4j
@Service
public class ExcelReportService implements ReportGenerator {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateMedicationsReport(List<MedicationDto> medications) throws IOException {
        log.info("Генерация отчета о лекарствах с {} записями", medications.size());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Лекарства");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Наименование", "Форма", "Цена", "Срок годности"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (MedicationDto medication : medications) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(medication.getId());
            row.createCell(1).setCellValue(medication.getName());
            row.createCell(2).setCellValue(medication.getForm().name());
            row.createCell(3).setCellValue(medication.getPrice());
            Cell dateCell = row.createCell(4);
            dateCell.setCellValue(dateFormat.format(medication.getExpirationDate()));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        log.info("Отчет о лекарствах успешно сгенерирован");

        return outputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateOrdersReport(List<OrderDto> orders) throws IOException {
        log.info("Генерация отчета о заказах с {} записями", orders.size());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Заказы");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Количество", "Общая сумма", "Дата заказа", "Статус"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (OrderDto order : orders) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(order.getId());
            row.createCell(1).setCellValue(order.getQuantity());
            row.createCell(2).setCellValue(order.getTotalAmount());
            Cell dateCell = row.createCell(3);
            dateCell.setCellValue(dateFormat.format(order.getOrderDate()));
            row.createCell(4).setCellValue(order.getStatus().name());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        log.info("Отчет о заказах успешно сгенерирован");

        return outputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateTotalOrdersReport(TotalOrders totalOrders) throws IOException {
        log.info("Генерация отчета о общем количестве заказов");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Общее количество заказов");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"Общее количество", "Общая сумма"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(totalOrders.getTotalQuantity());
        row.createCell(1).setCellValue(totalOrders.getTotalAmount());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        log.info("Отчет о общем количестве заказов успешно сгенерирован");

        return outputStream.toByteArray();
    }
}
