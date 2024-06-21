package ru.bakht.pharmacy.service.service.report;

import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для генерации отчетов в формате Excel/PDF.
 */
public interface ReportGenerator {

    /**
     * Генерирует отчет по медикаментам.
     *
     * @param medications список медикаментов для включения в отчет
     * @return байтовый массив, представляющий Excel/PDF файл
     * @throws IOException если произошла ошибка ввода-вывода при создании отчета
     */
    byte[] generateMedicationsReport(List<MedicationDto> medications) throws IOException;

    /**
     * Генерирует отчет по заказам.
     *
     * @param orders список заказов для включения в отчет
     * @return байтовый массив, представляющий Excel/PDF файл
     * @throws IOException если произошла ошибка ввода-вывода при создании отчета
     */
    byte[] generateOrdersReport(List<OrderDto> orders) throws IOException;

    /**
     * Генерирует отчет по общему количеству и сумме заказов.
     *
     * @param totalOrders объект, содержащий общее количество и сумму заказов
     * @return байтовый массив, представляющий Excel/PDF файл
     * @throws IOException если произошла ошибка ввода-вывода при создании отчета
     */
    byte[] generateTotalOrdersReport(TotalOrders totalOrders) throws IOException;
}
