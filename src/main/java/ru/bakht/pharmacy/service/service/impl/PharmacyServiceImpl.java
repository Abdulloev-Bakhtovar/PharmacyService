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
 * Реализация интерфейса PharmacyService.
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
        log.info("Fetching all pharmacies");
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
        log.info("Fetching pharmacy with id {}", id);
        return pharmacyRepository.findById(id)
                .map(pharmacyMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Pharmacy with id {} not found", id);
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
            log.info("Pharmacy with id {} already exists, updating pharmacy", id);
            return updatePharmacy(id, pharmacyDto);
        }

        log.info("Creating new pharmacy: {}", pharmacyDto);
        var pharmacy = pharmacyRepository.save(pharmacyMapper.toEntity(pharmacyDto));
        log.info("Created pharmacy: {}", pharmacy);
        return pharmacyMapper.toDto(pharmacy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PharmacyDto updatePharmacy(Long id, PharmacyDto pharmacyDto) {
        log.info("Updating pharmacy: {}", pharmacyDto);
        var existingPharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pharmacy with id {} not found", id);
                    return new EntityNotFoundException("Аптека", id);
                });

        updatePharmacyFromDto(existingPharmacy, pharmacyDto);
        return pharmacyMapper.toDto(pharmacyRepository.save(existingPharmacy));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePharmacyById(Long id) {
        log.info("Deleting pharmacy with id {}", id);
        pharmacyRepository.findById(id).ifPresentOrElse(
                pharmacy -> {
                    pharmacyRepository.deleteById(id);
                    log.info("Deleted pharmacy with id {}", id);
                },
                () -> {
                    log.error("Pharmacy with id {} not found", id);
                    throw new EntityNotFoundException("Аптека", id);
                }
        );
    }

    /**
     * {@inheritDoc}
     */
    public void addMedication(PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Creating or updating medication in pharmacy: {}", pharmacyMedicationDto);

        var pharmacyDto = getPharmacyById(pharmacyMedicationDto.getPharmacyDto().getId());
        var pharmacy = pharmacyMapper.toEntity(pharmacyDto);
        var medication = getMedicationById(pharmacyMedicationDto.getMedicationDto().getId());

        var pharmacyMedicationId = new PharmacyMedicationId(pharmacy.getId(), medication.getId());
        var existingPharmacyMedication = entityManager.find(PharmacyMedication.class, pharmacyMedicationId);

        if (existingPharmacyMedication != null) {
            log.info("PharmacyMedication with pharmacyId {} and medicationId {} already exists, updating quantity",
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
            log.info("Created new medication: {} in pharmacy: {}", medication, pharmacy);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateMedication(PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Updating medication in pharmacy: {}", pharmacyMedicationDto);

        var pharmacyMedicationId = new PharmacyMedicationId(
                pharmacyMedicationDto.getPharmacyDto().getId(),
                pharmacyMedicationDto.getMedicationDto().getId()
        );

        var existingPharmacyMedication = entityManager.find(PharmacyMedication.class, pharmacyMedicationId);

        if (existingPharmacyMedication == null) {
            log.error("PharmacyMedication with pharmacyId {} and medicationId {} not found",
                    pharmacyMedicationDto.getPharmacyDto().getId(),
                    pharmacyMedicationDto.getMedicationDto().getId());
            throw new EntityNotFoundException("Запись PharmacyMedication",
                    pharmacyMedicationDto.getPharmacyDto().getId(), pharmacyMedicationDto.getMedicationDto().getId());
        }

        existingPharmacyMedication.setQuantity(pharmacyMedicationDto.getQuantity());
        entityManager.merge(existingPharmacyMedication);
        log.info("Updated medication: {} in pharmacy: {}", pharmacyMedicationDto.getMedicationDto(), pharmacyMedicationDto.getPharmacyDto());
    }

    /**
     * {@inheritDoc}
     */
    public void removeMedication(PharmacyMedicationDto pharmacyMedicationDto) {
        log.info("Removing medication from pharmacy: {}", pharmacyMedicationDto);

        var pharmacyMedicationId = new PharmacyMedicationId(
                pharmacyMedicationDto.getPharmacyDto().getId(),
                pharmacyMedicationDto.getMedicationDto().getId()
        );

        var pharmacyMedication = entityManager.find(PharmacyMedication.class, pharmacyMedicationId);
        if (pharmacyMedication == null) {
            log.error("PharmacyMedication with pharmacyId {} and medicationId {} not found",
                    pharmacyMedicationDto.getPharmacyDto().getId(),
                    pharmacyMedicationDto.getMedicationDto().getId());
            throw new EntityNotFoundException("Запись PharmacyMedication",
                    pharmacyMedicationDto.getPharmacyDto().getId(), pharmacyMedicationDto.getMedicationDto().getId());
        }

        entityManager.remove(pharmacyMedication);
        log.info("Removed medication with id {} from pharmacy with id {}",
                pharmacyMedicationDto.getMedicationDto().getId(),
                pharmacyMedicationDto.getPharmacyDto().getId());
    }

    /**
     * Обновляет информацию об аптеке на основе данных из DTO.
     *
     * @param pharmacy объект Pharmacy, который необходимо обновить
     * @param pharmacyDto объект PharmacyDto с новыми данными
     */
    private void updatePharmacyFromDto(Pharmacy pharmacy, PharmacyDto pharmacyDto) {
        pharmacy.setName(pharmacyDto.getName());
        pharmacy.setAddress(pharmacyDto.getAddress());
        pharmacy.setPhone(pharmacyDto.getPhone());
    }

    private Medication getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Medication with id {} not found", id);
                    return new EntityNotFoundException("Лекарство", id);
                });
    }
}
