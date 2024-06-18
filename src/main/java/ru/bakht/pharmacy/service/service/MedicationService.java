package ru.bakht.pharmacy.service.service;

import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;

import java.util.List;

/**
 * Интерфейс для управления лекарствами.
 */
public interface MedicationService {

    /**
     * Возвращает список всех лекарств.
     *
     * @return список объектов MedicationDto.
     */
    List<MedicationDto> getAllMedications();

    /**
     * Ищет лекарство по его идентификатору.
     *
     * @param id идентификатор лекарства.
     * @return объект MedicationDto, если лекарство найдено.
     * @throws EntityNotFoundException если лекарство с указанным идентификатором не найдено.
     */
    MedicationDto getMedicationById(Long id);

    /**
     * Создает новое лекарство.
     *
     * @param medicationDto данные для создания лекарства.
     * @return созданный объект MedicationDto.
     */
    MedicationDto createMedication(MedicationDto medicationDto);

    /**
     * Обновляет существующее лекарство.
     *
     * @param id идентификатор лекарства.
     * @param medicationDto данные для обновления лекарства.
     * @return обновленный объект MedicationDto.
     * @throws EntityNotFoundException если лекарство с указанным идентификатором не найдено.
     */
    MedicationDto updateMedication(Long id, MedicationDto medicationDto);

    /**
     * Удаляет лекарство по его идентификатору.
     *
     * @param id идентификатор лекарства.
     * @throws EntityNotFoundException если лекарство с указанным идентификатором не найдено.
     */
    void deleteMedicationById(Long id);
}
