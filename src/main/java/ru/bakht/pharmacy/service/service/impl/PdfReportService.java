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
        log.info("Generating medications PDF report with {} entries", medications.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Medications Report"));
        Table table = new Table(new float[]{1, 3, 2, 2, 3});
        table.addHeaderCell("ID");
        table.addHeaderCell("Name");
        table.addHeaderCell("Form");
        table.addHeaderCell("Price");
        table.addHeaderCell("Expiration Date");

        for (MedicationDto medication : medications) {
            table.addCell(medication.getId().toString());
            table.addCell(medication.getName());
            table.addCell(medication.getForm().name());
            table.addCell(medication.getPrice().toString());
            table.addCell(dateFormat.format(medication.getExpirationDate()));
        }

        document.add(table);
        document.close();

        log.info("Medications PDF report generated successfully");

        return outputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateOrdersReport(List<OrderDto> orders) throws IOException {
        log.info("Generating orders PDF report with {} entries", orders.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Orders Report"));
        Table table = new Table(new float[]{1, 2, 2, 3, 2});
        table.addHeaderCell("ID");
        table.addHeaderCell("Quantity");
        table.addHeaderCell("Total Amount");
        table.addHeaderCell("Order Date");
        table.addHeaderCell("Status");

        for (OrderDto order : orders) {
            table.addCell(order.getId().toString());
            table.addCell(order.getQuantity().toString());
            table.addCell(order.getTotalAmount().toString());
            table.addCell(dateFormat.format(order.getOrderDate()));
            table.addCell(order.getStatus().name());
        }

        document.add(table);
        document.close();

        log.info("Orders PDF report generated successfully");

        return outputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateTotalOrdersReport(TotalOrders totalOrders) throws IOException {
        log.info("Generating total orders PDF report");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Total Orders Report"));
        Table table = new Table(new float[]{2, 2});
        table.addHeaderCell("Total Quantity");
        table.addHeaderCell("Total Amount");

        table.addCell(totalOrders.getTotalQuantity().toString());
        table.addCell(totalOrders.getTotalAmount().toString());

        document.add(table);
        document.close();

        log.info("Total orders PDF report generated successfully");

        return outputStream.toByteArray();
    }
}
