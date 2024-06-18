package ru.bakht.pharmacy.service.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.PharmacyMapper;
import ru.bakht.pharmacy.service.model.Medication;
import ru.bakht.pharmacy.service.model.Pharmacy;
import ru.bakht.pharmacy.service.model.PharmacyMedication;
import ru.bakht.pharmacy.service.model.PharmacyMedicationId;
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;
import ru.bakht.pharmacy.service.model.dto.PharmacyMedicationDto;
import ru.bakht.pharmacy.service.repository.MedicationRepository;
import ru.bakht.pharmacy.service.repository.PharmacyRepository;
import ru.bakht.pharmacy.service.service.PharmacyService;

import java.util.List;

/**
 * Реализация интерфейса {@link PharmacyService} для управления данными об аптеках.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final MedicationRepository medicationRepository;
    private final PharmacyMapper pharmacyMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<PharmacyDto> getAllPharmacies() {
        log.info("Получение списка всех аптек");
        return pharmacyRepository.findAll().stream()
                .map(pharmacyMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PharmacyDto getPharmacyById(Long id) {
        log.info("Получение аптеки с id {}", id);
        return pharmacyRepository.findById(id)
                .map(pharmacyMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Аптека с id {} не найдена", id);
                    return new EntityNotFoundException("Аптека", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PharmacyDto createPharmacy(PharmacyDto pharmacyDto) {
        var id = pharmacyDto.getId();

        if (id != null && pharmacyRepository.existsById(id)) {
            log.info("Аптека с id {} уже существует, выполняется обновление данных", id);
            return updatePharmacy(id, pharmacyDto);
        }

        log.info("Создание новой аптеки: {}", pharmacyDto);
        var pharmacy = pharmacyRepository.save(pharmacyMapper.toEntity(pharmacyDto));
        return pharmacyMapper.toDto(pharmacy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PharmacyDto updatePharmacy(Long id, PharmacyDto pharmacyDto) {
        log.info("Обновление данных аптеки: {}", pharmacyDto);
        var existingPharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Аптека с id {} не найдена", id);
                    return new EntityNotFoundException("Аптека", id);
                });

        pharmacyMapper.updateEntityFromDto(pharmacyDto, existingPharmacy);
        return pharmacyMapper.toDto(pharmacyRepository.save(existingPharmacy));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePharmacyById(Long id) {
        log.info("Удаление аптеки с id {}", id);
        pharmacyRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void addMedication(PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Создание или обновление записи о лекарстве в аптеке: {}", pharmacyMedicationDto);

        var pharmacyDto = getPharmacyById(pharmacyMedicationDto.getPharmacyDto().getId());
        var pharmacy = pharmacyMapper.toEntity(pharmacyDto);
        var medication = getMedicationById(pharmacyMedicationDto.getMedicationDto().getId());

        var pharmacyMedicationId = new PharmacyMedicationId(pharmacy.getId(), medication.getId());
        var existingPharmacyMedication = entityManager.find(PharmacyMedication.class, pharmacyMedicationId);

        if (existingPharmacyMedication != null) {
            log.info("Запись PharmacyMedication с pharmacyId {} и medicationId {} уже существует, обновление количества",
                    pharmacy.getId(), medication.getId());
            existingPharmacyMedication.setQuantity(pharmacyMedicationDto.getQuantity());
            entityManager.merge(existingPharmacyMedication);
        } else {
            var pharmacyMedication = PharmacyMedication.builder()
                    .id(pharmacyMedicationId)
                    .pharmacy(pharmacy)
                    .medication(medication)
                    .quantity(pharmacyMedicationDto.getQuantity())
                    .build();
            entityManager.persist(pharmacyMedication);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateMedication(PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Обновление данных о лекарстве в аптеке: {}", pharmacyMedicationDto);

        var pharmacyId = pharmacyMedicationDto.getPharmacyDto().getId();
        var medicationId = pharmacyMedicationDto.getMedicationDto().getId();
        var pharmacyMedicationId = new PharmacyMedicationId(pharmacyId, medicationId);

        var existingPharmacyMedication = entityManager.find(PharmacyMedication.class, pharmacyMedicationId);

        if (existingPharmacyMedication == null) {
            log.error("Запись PharmacyMedication с pharmacyId {} и medicationId {} не найдена",
                    pharmacyId, medicationId);
            throw new EntityNotFoundException("Запись PharmacyMedication", pharmacyId, medicationId);
        }

        existingPharmacyMedication.setQuantity(pharmacyMedicationDto.getQuantity());
        entityManager.merge(existingPharmacyMedication);
    }

    /**
     * {@inheritDoc}
     */
    public void removeMedication(PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Удаление лекарства из аптеки: {}", pharmacyMedicationDto);

        var pharmacyId = pharmacyMedicationDto.getPharmacyDto().getId();
        var medicationId = pharmacyMedicationDto.getMedicationDto().getId();
        var pharmacyMedicationId = new PharmacyMedicationId(pharmacyId, medicationId);

        var pharmacyMedication = entityManager.find(PharmacyMedication.class, pharmacyMedicationId);
        if (pharmacyMedication == null) {
            log.error("Запись PharmacyMedication с pharmacyId {} и medicationId {} не найдена",
                    pharmacyId, medicationId);
            throw new EntityNotFoundException("Запись PharmacyMedication", pharmacyId, medicationId);
        }

        entityManager.remove(pharmacyMedication);
    }

    /**
     * Получает лекарство по его идентификатору.
     *
     * @param id идентификатор лекарства
     * @return объект Medication
     * @throws EntityNotFoundException если лекарство не найдено
     */
    private Medication getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Лекарство с id {} не найдено", id);
                    return new EntityNotFoundException("Лекарство", id);
                });
    }
}
