package ru.bakht.pharmacy.service.service.report;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bakht.pharmacy.service.model.Employee;
import ru.bakht.pharmacy.service.model.PharmacyMedication;
import ru.bakht.pharmacy.service.repository.EmployeeRepository;
import ru.bakht.pharmacy.service.repository.MedicationRepository;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * Класс для проверки запасов медикаментов и уведомления сотрудников.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryCheckService {

    private final MedicationRepository medicationRepository;
    private final EmployeeRepository employeeRepository;
    private final JavaMailSender mailSender;
    private final RedisLockRegistry redisLockRegistry;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    private static final int THRESHOLD = 10; // минимальное количество медикаментов

    /**
     * Проверяет запасы медикаментов в аптеке и отправляет уведомления сотрудникам, если запасы ниже порогового значения.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkInventory() {
        if (isEmailConfigured()) {
            Lock lock = redisLockRegistry.obtain("inventoryCheckLock");

            try {
                if (lock.tryLock()) {
                    log.info("Начало проверки запасов медикаментов...");

                    List<PharmacyMedication> lowStockMedications =
                            medicationRepository.findMedicationsBelowThreshold(THRESHOLD);

                    if (!lowStockMedications.isEmpty()) {
                        sendNotifications(lowStockMedications);
                    }

                    log.info("Проверка запасов медикаментов завершена.");
                } else {
                    log.info("Другой экземпляр уже выполняет проверку запасов медикаментов.");
                }
            } catch (Exception e) {
                log.error("Ошибка во время проверки запасов медикаментов", e);

            } finally {
                try {
                    lock.unlock();
                } catch (IllegalStateException e) {
                    log.warn("Не удалось освободить блокировку. "
                            + "Блокировка не удерживается этим экземпляром.", e);
                }
            }
        } else {
            log.warn("Параметры электронной почты не настроены. Пропуск проверки запасов медикаментов.");
        }
    }

    /**
     * Отправляет уведомления сотрудникам аптеки о низких запасах медикаментов.
     *
     * @param medications список медикаментов с низким запасом
     */
    public void sendNotifications(List<PharmacyMedication> medications) {
        medications.stream()
                .collect(Collectors.groupingBy(pm -> pm.getPharmacy().getId()))
                .forEach((pharmacyId, pharmacyMedications) -> {
                    List<Employee> employees = employeeRepository.findByPharmacyId(pharmacyId);
                    employees.forEach(employee -> {
                        String subject = "Уведомление о низком запасе медикаментов";
                        StringBuilder message = new StringBuilder("Следующие медикаменты имеют низкий запас:\n\n");

                        for (PharmacyMedication pm : pharmacyMedications) {
                            message.append("ID: ").append(pm.getMedication().getId())
                                    .append(", Наименование: ").append(pm.getMedication().getName())
                                    .append(", Форма выпуска: ").append(pm.getMedication().getForm().name())
                                    .append(", Цена: ").append(pm.getMedication().getPrice())
                                    .append(", Количество: ").append(pm.getQuantity())
                                    .append("\n");
                        }

                        try {
                            sendEmail(employee.getEmail(), subject, message.toString());
                        } catch (MessagingException e) {
                            log.error("Не удалось отправить уведомление о низком запасе на почту {}", employee.getEmail(), e);
                        }
                    });
                });
    }

    /**
     * Отправляет электронное письмо.
     *
     * @param to      адрес получателя
     * @param subject тема письма
     * @param text    текст письма
     * @throws MessagingException если произошла ошибка при отправке письма
     */
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        mailSender.send(message);
        log.info("Уведомление о низком запасе медикаментов отправлено на адрес {}", to);
    }

    private boolean isEmailConfigured() {
        return mailUsername != null && !mailUsername.isEmpty() &&
                mailPassword != null && !mailPassword.isEmpty();
    }
}
