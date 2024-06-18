package ru.bakht.pharmacy.service.service.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.extern.slf4j.Slf4j;
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
public class PdfReportService implements ReportGenerator {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateMedicationsReport(List<MedicationDto> medications) throws IOException {
        log.info("Генерация PDF-отчета по лекарствам с {} записями", medications.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Отчет по лекарствам"));
        Table table = new Table(new float[]{1, 3, 2, 2, 3});
        table.addHeaderCell("ID");
        table.addHeaderCell("Наименование");
        table.addHeaderCell("Форма");
        table.addHeaderCell("Цена");
        table.addHeaderCell("Дата истечения срока");

        for (MedicationDto medication : medications) {
            table.addCell(medication.getId().toString());
            table.addCell(medication.getName());
            table.addCell(medication.getForm().name());
            table.addCell(medication.getPrice().toString());
            table.addCell(dateFormat.format(medication.getExpirationDate()));
        }

        document.add(table);
        document.close();

        log.info("PDF-отчет по лекарствам успешно сгенерирован");

        return outputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateOrdersReport(List<OrderDto> orders) throws IOException {
        log.info("Генерация PDF-отчета по заказам с {} записями", orders.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Отчет по заказам"));
        Table table = new Table(new float[]{1, 2, 2, 3, 2});
        table.addHeaderCell("ID");
        table.addHeaderCell("Количество");
        table.addHeaderCell("Общая сумма");
        table.addHeaderCell("Дата заказа");
        table.addHeaderCell("Статус");

        for (OrderDto order : orders) {
            table.addCell(order.getId().toString());
            table.addCell(order.getQuantity().toString());
            table.addCell(order.getTotalAmount().toString());
            table.addCell(dateFormat.format(order.getOrderDate()));
            table.addCell(order.getStatus().name());
        }

        document.add(table);
        document.close();

        log.info("PDF-отчет по заказам успешно сгенерирован");

        return outputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateTotalOrdersReport(TotalOrders totalOrders) throws IOException {
        log.info("Генерация PDF-отчета по общему числу заказов");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Отчет по общему числу заказов"));
        Table table = new Table(new float[]{2, 2});
        table.addHeaderCell("Общее количество");
        table.addHeaderCell("Общая сумма");

        table.addCell(totalOrders.getTotalQuantity().toString());
        table.addCell(totalOrders.getTotalAmount().toString());

        document.add(table);
        document.close();

        log.info("PDF-отчет по общему числу заказов успешно сгенерирован");

        return outputStream.toByteArray();
    }
}
