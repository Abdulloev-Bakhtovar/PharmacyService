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

@Slf4j
@Service
public class ExcelReportService implements ReportGenerator {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateMedicationsReport(List<MedicationDto> medications) throws IOException {
        log.info("Generating medications report with {} entries", medications.size());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Medications");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Name", "Form", "Price", "Expiration Date"};
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

        log.info("Medications report generated successfully");

        return outputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateOrdersReport(List<OrderDto> orders) throws IOException {
        log.info("Generating orders report with {} entries", orders.size());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Orders");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Quantity", "Total Amount", "Order Date", "Status"};
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

        log.info("Orders report generated successfully");

        return outputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateTotalOrdersReport(TotalOrders totalOrders) throws IOException {
        log.info("Generating total orders report");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Total Orders");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"Total Quantity", "Total Amount"};
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

        log.info("Total orders report generated successfully");

        return outputStream.toByteArray();
    }
}
