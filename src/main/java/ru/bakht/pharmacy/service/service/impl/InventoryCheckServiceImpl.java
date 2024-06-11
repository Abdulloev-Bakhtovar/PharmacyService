package ru.bakht.pharmacy.service.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bakht.pharmacy.service.model.Employee;
import ru.bakht.pharmacy.service.model.PharmacyMedication;
import ru.bakht.pharmacy.service.repository.EmployeeRepository;
import ru.bakht.pharmacy.service.repository.MedicationRepository;
import ru.bakht.pharmacy.service.service.InventoryCheckService;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link InventoryCheckService} для проверки запасов медикаментов и уведомления сотрудников.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryCheckServiceImpl implements InventoryCheckService {

    private final MedicationRepository medicationRepository;
    private final EmployeeRepository employeeRepository;
    private final JavaMailSender mailSender;
    private final RedisLockRegistry redisLockRegistry;

    private static final int THRESHOLD = 10;

    /**
     * {@inheritDoc}
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void checkInventory() {
        Lock lock = redisLockRegistry.obtain("inventoryCheckLock");

        try {
            if (lock.tryLock()) {
                log.info("Starting inventory check...");

                List<PharmacyMedication> lowStockMedications =
                        medicationRepository.findMedicationsBelowThreshold(THRESHOLD);

                if (!lowStockMedications.isEmpty()) {
                    sendNotifications(lowStockMedications);
                }

                log.info("Inventory check completed.");
            } else {
                log.info("Another instance is already performing the inventory check.");
            }
        } catch (Exception e) {
            log.error("Error during inventory check", e);
        } finally {
            try {
                lock.unlock();
            } catch (IllegalStateException e) {
                log.warn("Failed to release the lock. The lock is not held by this instance.", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendNotifications(List<PharmacyMedication> medications) {
        medications.stream()
                .collect(Collectors.groupingBy(pm -> pm.getPharmacy().getId()))
                .forEach((pharmacyId, pharmacyMedications) -> {
                    List<Employee> employees = employeeRepository.findByPharmacyId(pharmacyId);
                    employees.forEach(employee -> {
                        String subject = "Low Stock Medication Notification";
                        StringBuilder message = new StringBuilder("The following medications have low stock:\n\n");

                        for (PharmacyMedication pm : pharmacyMedications) {
                            message.append("ID: ").append(pm.getMedication().getId())
                                    .append(", Name: ").append(pm.getMedication().getName())
                                    .append(", Form: ").append(pm.getMedication().getForm().name())
                                    .append(", Price: ").append(pm.getMedication().getPrice())
                                    .append(", Quantity: ").append(pm.getQuantity())
                                    .append("\n");
                        }

                        try {
                            sendEmail(employee.getEmail(), subject, message.toString());
                        } catch (MessagingException e) {
                            log.error("Failed to send low stock notification to {}", employee.getEmail(), e);
                        }
                    });
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        mailSender.send(message);
        log.info("Low stock medication notification sent to {}", to);
    }
}
