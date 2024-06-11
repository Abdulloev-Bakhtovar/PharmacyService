package ru.bakht.pharmacy.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.MedicationMapper;
import ru.bakht.pharmacy.service.model.Medication;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.repository.MedicationRepository;
import ru.bakht.pharmacy.service.service.MedicationService;

import java.util.List;

/**
 * Реализация интерфейса MedicationService.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MedicationDto> getAllMedications() {
        log.info("Fetching all medications");
        return medicationRepository.findAll().stream()
                .map(medicationMapper::toDto)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MedicationDto getMedicationById(Long id) {
        log.info("Fetching medication with id {}", id);
        return medicationRepository.findById(id)
                .map(medicationMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Medication with id {} not found", id);
                    return new EntityNotFoundException("Лекарство", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MedicationDto createMedication(MedicationDto medicationDto) {
        var id = medicationDto.getId();

        if (id != null && medicationRepository.existsById(id)) {
            log.info("Medication with id {} already exists, updating medication", id);
            return updateMedication(id, medicationDto);
        }

        log.info("Creating new medication: {}", medicationDto);
        var medication = medicationRepository.save(medicationMapper.toEntity(medicationDto));
        log.info("Created medication: {}", medication);
        return medicationMapper.toDto(medication);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MedicationDto updateMedication(Long id, MedicationDto medicationDto) {
        log.info("Updating medication: {}", medicationDto);
        var existingMedication = medicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Medication with id {} not found", id);
                    return new EntityNotFoundException("Лекарство", id);
                });

        updateMedicationFromDto(existingMedication, medicationDto);
        return medicationMapper.toDto(medicationRepository.save(existingMedication));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMedicationById(Long id) {
        log.info("Deleting medication with id {}", id);
        medicationRepository.findById(id).ifPresentOrElse(
                medication -> {
                    medicationRepository.deleteById(id);
                    log.info("Deleted medication with id {}", id);
                },
                () -> {
                    log.error("Medication with id {} not found", id);
                    throw new EntityNotFoundException("Лекарство", id);
                }
        );
    }

    /**
     * Обновляет информацию о лекарстве на основе данных из DTO.
     *
     * @param medication объект Medication, который необходимо обновить
     * @param medicationDto объект MedicationDto с новыми данными
     */
    private void updateMedicationFromDto(Medication medication, MedicationDto medicationDto) {
        medication.setName(medicationDto.getName());
        medication.setForm(medicationDto.getForm());
        medication.setPrice(medicationDto.getPrice());
        medication.setExpirationDate(medicationDto.getExpirationDate());
    }
}
