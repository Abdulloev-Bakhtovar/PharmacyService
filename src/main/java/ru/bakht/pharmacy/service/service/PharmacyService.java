package ru.bakht.pharmacy.service.service;

import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;
import ru.bakht.pharmacy.service.model.dto.PharmacyMedicationDto;

import java.util.List;

/**
 * Интерфейс для управления аптеками.
 */
public interface PharmacyService {

    /**
     * Возвращает список всех аптек.
     *
     * @return список объектов PharmacyDto.
     */
    List<PharmacyDto> getAllPharmacies();

    /**
     * Ищет аптеку по ее идентификатору.
     *
     * @param id идентификатор аптеки.
     * @return объект PharmacyDto, если аптека найдена.
     * @throws EntityNotFoundException если аптека с указанным идентификатором не найдена.
     */
    PharmacyDto getPharmacyById(Long id);

    /**
     * Создает новую аптеку.
     *
     * @param pharmacyDto данные для создания аптеки.
     * @return созданный объект PharmacyDto.
     */
    PharmacyDto createPharmacy(PharmacyDto pharmacyDto);

    /**
     * Обновляет существующую аптеку.
     *
     * @param id идентификатор аптеки.
     * @param pharmacyDto данные для обновления аптеки.
     * @return обновленный объект PharmacyDto.
     * @throws EntityNotFoundException если аптека с указанным идентификатором не найдена.
     */
    PharmacyDto updatePharmacy(Long id, PharmacyDto pharmacyDto);

    /**
     * Удаляет аптеку по ее идентификатору.
     *
     * @param id идентификатор аптеки.
     * @throws EntityNotFoundException если аптека с указанным идентификатором не найдена.
     */
    void deletePharmacyById(Long id);

    /**
     * Добавляет лекарство в аптеку.
     *
     * @param pharmacyMedicationDto DTO с информацией о лекарстве и аптеке
     */
    void addMedication(PharmacyMedicationDto pharmacyMedicationDto);

    /**
     * Обновляет количество лекарства в аптеке.
     *
     * @param pharmacyMedicationDto DTO с информацией о лекарстве и аптеке
     */
    void updateMedication(PharmacyMedicationDto pharmacyMedicationDto);

    /**
     * Удаляет лекарство из аптеки.
     *
     * @param pharmacyMedicationDto DTO с информацией о лекарстве и аптеке
     */
    void removeMedication(PharmacyMedicationDto pharmacyMedicationDto);
}
