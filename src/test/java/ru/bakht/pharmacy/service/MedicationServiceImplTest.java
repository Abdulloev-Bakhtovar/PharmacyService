package ru.bakht.pharmacy.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bakht.pharmacy.service.enums.Form;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.MedicationMapper;
import ru.bakht.pharmacy.service.model.Medication;
import ru.bakht.pharmacy.service.model.dto.MedicationDto;
import ru.bakht.pharmacy.service.repository.MedicationRepository;
import ru.bakht.pharmacy.service.service.impl.MedicationServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceImplTest {

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private MedicationMapper medicationMapper;

    @InjectMocks
    private MedicationServiceImpl medicationService;

    @Test
    @DisplayName("getAllMedications returns list of all medications")
    void getAllMedications_ReturnsAllMedications() {
        Medication medication = new Medication(1L, "Aspirin", Form.TABLET, 100.0, null);
        MedicationDto medicationDto = new MedicationDto(1L, "Aspirin", Form.TABLET, 100.0, null);

        when(medicationRepository.findAll()).thenReturn(List.of(medication));
        when(medicationMapper.toDto(any(Medication.class))).thenReturn(medicationDto);

        List<MedicationDto> medications = medicationService.getAllMedications();

        assertEquals(1, medications.size());
        assertEquals(1L, medications.getFirst().getId());
        assertEquals("Aspirin", medications.getFirst().getName());
        assertEquals(Form.TABLET, medications.getFirst().getForm());
        assertEquals(100.0, medications.getFirst().getPrice());

        verify(medicationRepository, times(1)).findAll();
        verify(medicationMapper, times(1)).toDto(any(Medication.class));
    }

    @Test
    void getMedicationById_ReturnsMedication() {
        Medication medication = new Medication(1L, "Aspirin", Form.TABLET, 100.0,null);
        MedicationDto medicationDto = new MedicationDto(1L, "Aspirin", Form.TABLET, 100.0, null);

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(medicationMapper.toDto(any(Medication.class))).thenReturn(medicationDto);

        MedicationDto foundMedication = medicationService.getMedicationById(1L);

        assertEquals(1L, foundMedication.getId());
        assertEquals("Aspirin", foundMedication.getName());
        assertEquals(Form.TABLET, foundMedication.getForm());
        assertEquals(100.0, foundMedication.getPrice());

        verify(medicationRepository, times(1)).findById(1L);
        verify(medicationMapper, times(1)).toDto(any(Medication.class));
    }

    @Test
    void getMedicationById_ReturnsMedicationNotFound() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> medicationService.getMedicationById(1L),
                "Expected getMedicationById to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Лекарство с ID 1 не найден"),
                "Expected exception message to contain 'Лекарство с ID 1 не найден'");

        verify(medicationRepository, times(1)).findById(1L);
        verifyNoInteractions(medicationMapper);
    }



    @Test
    void createMedication_ReturnsMedication() {
        Medication medication = new Medication(null, "Aspirin", Form.TABLET, 100.0, null);
        Medication savedMedication = new Medication(1L, "Aspirin", Form.TABLET, 100.0, null);
        MedicationDto medicationDto = new MedicationDto(null, "Aspirin", Form.TABLET, 100.0, null);
        MedicationDto savedMedicationDto = new MedicationDto(1L, "Aspirin", Form.TABLET, 100.0, null);

        when(medicationMapper.toEntity(any(MedicationDto.class))).thenReturn(medication);
        when(medicationRepository.save(any(Medication.class))).thenReturn(savedMedication);
        when(medicationMapper.toDto(any(Medication.class))).thenReturn(savedMedicationDto);

        MedicationDto createdMedication = medicationService.createMedication(medicationDto);

        assertEquals(1L, createdMedication.getId());
        assertEquals("Aspirin", createdMedication.getName());
        assertEquals(Form.TABLET, createdMedication.getForm());
        assertEquals(100.0, createdMedication.getPrice());

        verify(medicationMapper, times(1)).toEntity(any(MedicationDto.class));
        verify(medicationRepository, times(1)).save(any(Medication.class));
        verify(medicationMapper, times(1)).toDto(any(Medication.class));
    }

    @Test
    void updateMedication_ReturnsMedication() {
        Medication existingMedication = new Medication(1L, "Aspirin", Form.TABLET, 100.0, null);
        Medication updatedMedication = new Medication(1L, "Ibuprofen", Form.CAPSULE, 150.0, null);
        MedicationDto medicationDto = new MedicationDto(1L, "Ibuprofen", Form.CAPSULE, 150.0, null);
        MedicationDto updatedMedicationDto = new MedicationDto(1L, "Ibuprofen", Form.CAPSULE, 150.0, null);

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(existingMedication));
        when(medicationRepository.save(any(Medication.class))).thenReturn(updatedMedication);
        when(medicationMapper.toDto(any(Medication.class))).thenReturn(updatedMedicationDto);

        MedicationDto result = medicationService.updateMedication(1L, medicationDto);

        assertEquals("Ibuprofen", result.getName());
        assertEquals(Form.CAPSULE, result.getForm());
        assertEquals(150.0, result.getPrice());
        verify(medicationRepository, times(1)).findById(1L);
        verify(medicationRepository, times(1)).save(any(Medication.class));
        verify(medicationMapper, times(1)).toDto(any(Medication.class));
    }

    @Test
    void deleteMedicationById_SuccessfulDeletion() {
        Long medicationId = 10L;

        medicationService.deleteMedicationById(medicationId);

        verify(medicationRepository, times(1)).deleteById(medicationId);
    }

}
