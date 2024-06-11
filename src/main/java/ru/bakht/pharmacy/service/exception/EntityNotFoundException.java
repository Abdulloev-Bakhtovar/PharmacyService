package ru.bakht.pharmacy.service.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Long entityId) {
        super(String.format("%s с ID %d не найден", entityName, entityId));
    }

    public EntityNotFoundException(String entityName, Long pharmacyId, Long medicationId) {
        super(String.format("%s с ID аптеки %d и ID лекарства %d не найдена", entityName, pharmacyId, medicationId));
    }
}
