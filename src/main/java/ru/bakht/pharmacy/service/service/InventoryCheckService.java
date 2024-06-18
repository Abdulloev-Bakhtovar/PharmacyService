package ru.bakht.pharmacy.service.service;

import jakarta.mail.MessagingException;
import ru.bakht.pharmacy.service.model.PharmacyMedication;

import java.util.List;

/**
 * Интерфейс для проверки запасов медикаментов в аптеке и уведомления сотрудников.
 */
public interface InventoryCheckService {

    /**
     * Проверяет запасы медикаментов в аптеке и отправляет уведомления сотрудникам, если запасы ниже порогового значения.
     */
    void checkInventory();

    /**
     * Отправляет уведомления сотрудникам аптеки о низких запасах медикаментов.
     *
     * @param medications список медикаментов с низким запасом
     */
    void sendNotifications(List<PharmacyMedication> medications);

    /**
     * Отправляет электронное письмо.
     *
     * @param to      адрес получателя
     * @param subject тема письма
     * @param text    текст письма
     * @throws MessagingException если произошла ошибка при отправке письма
     */
    void sendEmail(String to, String subject, String text) throws MessagingException;
}
