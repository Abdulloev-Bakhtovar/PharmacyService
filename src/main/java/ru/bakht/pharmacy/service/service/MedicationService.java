
package ru.bakht.pharmacy.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.MedicationMapper;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.repository.MedicationRepository;

import java.util.List;

/**
 * Реализация интерфейса {@link BaseService} для управления лекарствами.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MedicationService implements BaseService<MedicationDto, Long> {

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    /**

     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MedicationDto> getAll() {
        log.info("Получение всех лекарств");
        return medicationRepository.findAll().stream()
                .map(medicationMapper::toDto)
                .toList();
    }
    /**

     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MedicationDto getById(Long id) {
        log.info("Получение лекарства с идентификатором {}", id);
        return medicationRepository.findById(id)
                .map(medicationMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Лекарство с идентификатором {} не найдено", id);
                    return new EntityNotFoundException("Лекарство", id);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MedicationDto create(MedicationDto medicationDto) {
        var id = medicationDto.getId();

        if (id != null && medicationRepository.existsById(id)) {
            log.info("Лекарство с идентификатором {} уже существует, обновление лекарства", id);
            return update(id, medicationDto);
        }

        log.info("Создание нового лекарства: {}", medicationDto);
        var medication = medicationRepository.save(medicationMapper.toEntity(medicationDto));
        return medicationMapper.toDto(medication);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MedicationDto update(Long id, MedicationDto medicationDto) {
        log.info("Обновление лекарства: {}", medicationDto);
        var existingMedication = medicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Лекарство с идентификатором {} не найдено", id);
                    return new EntityNotFoundException("Лекарство", id);
                });

        medicationMapper.updateEntityFromDto(medicationDto, existingMedication);
        return medicationMapper.toDto(medicationRepository.save(existingMedication));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        log.info("Удаление лекарства с идентификатором {}", id);
        medicationRepository.deleteById(id);
    }
}