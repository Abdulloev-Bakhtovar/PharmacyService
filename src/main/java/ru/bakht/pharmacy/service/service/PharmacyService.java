package ru.bakht.pharmacy.service.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.MedicationMapper;
import ru.bakht.pharmacy.service.mapper.PharmacyMapper;
import ru.bakht.pharmacy.service.model.PharmacyMedication;
import ru.bakht.pharmacy.service.model.PharmacyMedicationId;
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;
import ru.bakht.pharmacy.service.model.dto.PharmacyMedicationDto;
import ru.bakht.pharmacy.service.repository.PharmacyRepository;

import java.util.List;

/**
 * Реализация интерфейса {@link BaseService} для управления данными об аптеках.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PharmacyService implements BaseService<PharmacyDto, Long> {

    private final PharmacyRepository pharmacyRepository;
    private final MedicationService medicationService;
    private final PharmacyMapper pharmacyMapper;
    private final MedicationMapper medicationMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<PharmacyDto> getAll() {
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
    public PharmacyDto getById(Long id) {
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
    public PharmacyDto create(PharmacyDto pharmacyDto) {
        var id = pharmacyDto.getId();

        if (id != null && pharmacyRepository.existsById(id)) {
            log.info("Аптека с id {} уже существует, выполняется обновление данных", id);
            return update(id, pharmacyDto);
        }

        log.info("Создание новой аптеки: {}", pharmacyDto);
        var pharmacy = pharmacyRepository.save(pharmacyMapper.toEntity(pharmacyDto));
        return pharmacyMapper.toDto(pharmacy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PharmacyDto update(Long id, PharmacyDto pharmacyDto) {
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
    public void delete(Long id) {
        log.info("Удаление аптеки с id {}", id);
        pharmacyRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void addOrUpdatePharmacyMedication(PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Создание или обновление записи о лекарстве в аптеке: {}", pharmacyMedicationDto);

        var pharmacy = pharmacyMapper.toEntity(
                getById(pharmacyMedicationDto.getPharmacyDto().getId())
        );
        var medication = medicationMapper.toEntity(
                medicationService.getById(pharmacyMedicationDto.getMedicationDto().getId())
        );

        var pharmacyMedicationId = new PharmacyMedicationId(pharmacy.getId(), medication.getId());
        var existingPharmacyMedication = entityManager.find(PharmacyMedication.class, pharmacyMedicationId);

        if (existingPharmacyMedication != null) {
            log.info("Запись PharmacyMedication с pharmacyId {} и medicationId {} уже существует, обновление количества",
                    pharmacy.getId(), medication.getId());

            existingPharmacyMedication.setQuantity(
                    existingPharmacyMedication.getQuantity() + pharmacyMedicationDto.getQuantity()
            );
            entityManager.merge(existingPharmacyMedication);
        } else {
            var pharmacyMedication = PharmacyMedication.builder()
                    .id(pharmacyMedicationId)
                    .pharmacy(pharmacy)
                    .medication(medication)
                    .quantity(pharmacyMedicationDto.getQuantity())
                    .build();
            entityManager.merge(pharmacyMedication);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deletePharmacyMedication(PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Удаление лекарства из аптеки: {}", pharmacyMedicationDto);

        var pharmacyId = pharmacyMedicationDto.getPharmacyDto().getId();
        var medicationId = pharmacyMedicationDto.getMedicationDto().getId();
        var pharmacyMedicationId = new PharmacyMedicationId(pharmacyId, medicationId);

        var pharmacyMedication = entityManager.find(PharmacyMedication.class, pharmacyMedicationId);

        if(pharmacyMedication != null) {
            entityManager.remove(pharmacyMedication);
        }
    }
}
