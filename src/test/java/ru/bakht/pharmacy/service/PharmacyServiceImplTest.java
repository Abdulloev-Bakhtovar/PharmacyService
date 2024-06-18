package ru.bakht.pharmacy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bakht.pharmacy.service.exception.EntityNotFoundException;
import ru.bakht.pharmacy.service.mapper.PharmacyMapper;
import ru.bakht.pharmacy.service.model.Pharmacy;
import ru.bakht.pharmacy.service.model.dto.PharmacyDto;
import ru.bakht.pharmacy.service.repository.PharmacyRepository;
import ru.bakht.pharmacy.service.service.impl.PharmacyServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PharmacyServiceImplTest {

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private PharmacyMapper pharmacyMapper;

    @InjectMocks
    private PharmacyServiceImpl pharmacyService;

    private Pharmacy pharmacy;
    private PharmacyDto pharmacyDto;

    @BeforeEach
    void setUp() {
        pharmacy = new Pharmacy(1L, "Аптека №1", "ул. Ленина, 2", "89007654321", null);
        pharmacyDto = new PharmacyDto(1L, "Аптека №1", "ул. Ленина, 2", "89007654321");
    }

    @Test
    void getAllPharmacies_ReturnsPharmacyDtoList() {
        when(pharmacyRepository.findAll()).thenReturn(List.of(pharmacy));
        when(pharmacyMapper.toDto(any(Pharmacy.class))).thenReturn(pharmacyDto);

        List<PharmacyDto> result = pharmacyService.getAllPharmacies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pharmacyDto, result.getFirst());
        verify(pharmacyRepository, times(1)).findAll();
        verify(pharmacyMapper, times(1)).toDto(any(Pharmacy.class));
    }

    @Test
    void getPharmacyById_ReturnsPharmacyDto() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(pharmacyMapper.toDto(any(Pharmacy.class))).thenReturn(pharmacyDto);

        PharmacyDto result = pharmacyService.getPharmacyById(1L);

        assertNotNull(result);
        assertEquals(pharmacyDto, result);
        verify(pharmacyRepository, times(1)).findById(1L);
        verify(pharmacyMapper, times(1)).toDto(any(Pharmacy.class));
    }

    @Test
    void getPharmacyById_ThrowsEntityNotFoundException() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> pharmacyService.getPharmacyById(1L)
        );

        assertTrue(thrown.getMessage().contains("Аптека"));
        verify(pharmacyRepository, times(1)).findById(1L);
    }

    @Test
    void createPharmacy_ReturnsPharmacyDto() {
        when(pharmacyRepository.existsById(1L)).thenReturn(false);
        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(pharmacy);
        when(pharmacyMapper.toEntity(any(PharmacyDto.class))).thenReturn(pharmacy);
        when(pharmacyMapper.toDto(any(Pharmacy.class))).thenReturn(pharmacyDto);

        PharmacyDto result = pharmacyService.createPharmacy(pharmacyDto);

        assertNotNull(result);
        assertEquals(pharmacyDto, result);
        verify(pharmacyRepository, times(1)).existsById(1L);
        verify(pharmacyRepository, times(1)).save(any(Pharmacy.class));
    }

    @Test
    void updatePharmacy_ReturnsPharmacyDto() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(pharmacy);
        when(pharmacyMapper.toDto(any(Pharmacy.class))).thenReturn(pharmacyDto);

        PharmacyDto result = pharmacyService.updatePharmacy(1L, pharmacyDto);

        assertNotNull(result);
        assertEquals(pharmacyDto, result);
        verify(pharmacyRepository, times(1)).findById(1L);
        verify(pharmacyRepository, times(1)).save(any(Pharmacy.class));
    }

    @Test
    void updatePharmacy_ThrowsEntityNotFoundException() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> pharmacyService.updatePharmacy(1L, pharmacyDto)
        );

        assertTrue(thrown.getMessage().contains("Аптека"));
        verify(pharmacyRepository, times(1)).findById(1L);
    }

    @Test
    void deletePharmacyById_SuccessfulDeletion() {
        Long pharmacyId = 10L;

        pharmacyService.deletePharmacyById(pharmacyId);

        verify(pharmacyRepository, times(1)).deleteById(pharmacyId);
    }
}
