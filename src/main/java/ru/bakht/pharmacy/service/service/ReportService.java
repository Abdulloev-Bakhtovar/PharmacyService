package ru.bakht.pharmacy.service.service;

import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.model.dto.OrderDto;
import ru.bakht.pharmacy.service.model.dto.TotalOrders;

import java.util.Date;
import java.util.List;

/**
 * Сервисный интерфейс для генерации отчетов, связанных с медикаментами и заказами.
 */
public interface ReportService {

    /**
     * Получает список медикаментов, доступных в конкретной аптеке.
     *
     * @param pharmacyId ID аптеки
     * @return список {@link MedicationDto}, представляющих медикаменты, доступные в аптеке
     */
    List<MedicationDto> getMedicationsByPharmacy(Long pharmacyId);

    /**
     * Получает общее количество и общую стоимость всех заказов за указанный период.
     *
     * @param startDate начальная дата периода
     * @param endDate конечная дата периода
     * @return объект {@link TotalOrders}, содержащий общее количество и общую стоимость заказов
     */
    TotalOrders getTotalQuantityAndAmount(Date startDate, Date endDate);

    /**
     * Получает список заказов, сделанных конкретным клиентом по его номеру телефона.
     *
     * @param phone номер телефона клиента
     * @return список {@link OrderDto}, представляющих заказы, сделанные клиентом
     */
    List<OrderDto> getOrdersByCustomerPhone(String phone);

    /**
     * Получает список медикаментов, которые закончились в аптека по id аптека.
     *
     * @param pharmacyId номер телефона клиента
     * @return список {@link MedicationDto}, представляющих медикаменты, которые закончились на складе
     */
    List<MedicationDto> getOutOfStockMedicationsByPharmacy(Long pharmacyId) ;
}
