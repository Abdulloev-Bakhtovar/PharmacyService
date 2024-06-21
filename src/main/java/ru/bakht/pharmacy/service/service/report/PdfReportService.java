package ru.bakht.pharmacy.service.service.report;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class PdfReportService implements ReportGenerator {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        PdfFont font = PdfFontFactory.createFont("static/arial-unicode-ms.ttf", PdfEncodings.IDENTITY_H);

        document.add(new Paragraph("Отчет по лекарствам").setFont(font));
        Table table = new Table(new float[]{1, 3, 2, 2, 3});
        table.addHeaderCell(new Paragraph("ID").setFont(font));
        table.addHeaderCell(new Paragraph("Наименование").setFont(font));
        table.addHeaderCell(new Paragraph("Форма").setFont(font));
        table.addHeaderCell(new Paragraph("Цена").setFont(font));
        table.addHeaderCell(new Paragraph("Дата истечения срока").setFont(font));

        for (MedicationDto medication : medications) {
            table.addCell(new Paragraph(medication.getId().toString()).setFont(font));
            table.addCell(new Paragraph(medication.getName()).setFont(font));
            table.addCell(new Paragraph(medication.getMedicationForm().name()).setFont(font));
            table.addCell(new Paragraph(medication.getPrice().toString()).setFont(font));
            table.addCell(new Paragraph(dateFormat.format(medication.getExpirationDate())).setFont(font));
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
        PdfFont font = PdfFontFactory.createFont("static/arial-unicode-ms.ttf", PdfEncodings.IDENTITY_H);

        document.add(new Paragraph("Отчет по заказам").setFont(font));
        Table table = new Table(new float[]{1, 3, 2, 2, 3, 2});
        table.addHeaderCell(new Paragraph("ID").setFont(font));
        table.addHeaderCell(new Paragraph("Названия").setFont(font));
        table.addHeaderCell(new Paragraph("Количество").setFont(font));
        table.addHeaderCell(new Paragraph("Общая сумма").setFont(font));
        table.addHeaderCell(new Paragraph("Дата заказа").setFont(font));
        table.addHeaderCell(new Paragraph("Статус").setFont(font));

        for (OrderDto order : orders) {
            table.addCell(new Paragraph(order.getId().toString()).setFont(font));
            table.addCell(new Paragraph(order.getMedicationDto().getName()).setFont(font));
            table.addCell(new Paragraph(order.getQuantity().toString()).setFont(font));
            table.addCell(new Paragraph(order.getTotalAmount().toString()).setFont(font));
            table.addCell(new Paragraph(dateFormat.format(order.getOrderDate())).setFont(font));
            table.addCell(new Paragraph(order.getOrderStatus().name()).setFont(font));
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
        PdfFont font = PdfFontFactory.createFont("static/arial-unicode-ms.ttf", PdfEncodings.IDENTITY_H);

        document.add(new Paragraph("Отчет по общему числу заказов").setFont(font));
        Table table = new Table(new float[]{2, 2});
        table.addHeaderCell(new Paragraph("Общее количество").setFont(font));
        table.addHeaderCell(new Paragraph("Общая сумма").setFont(font));

        table.addCell(new Paragraph(totalOrders.getTotalQuantity().toString()).setFont(font));
        table.addCell(new Paragraph(totalOrders.getTotalAmount().toString()).setFont(font));

        document.add(table);
        document.close();

        log.info("PDF-отчет по общему числу заказов успешно сгенерирован");

        return outputStream.toByteArray();
    }
}
