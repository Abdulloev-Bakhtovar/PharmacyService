package ru.bakht.pharmacy.service.service;

import ru.bakht.pharmacy.service.exception.EntityNotFoundException;

import java.util.List;

/**
 * Базовый интерфейс для управления сущностями.
 *
 * @param <T> тип DTO объекта.
 * @param <ID> тип идентификатора объекта.
 */
public interface BaseService<T, ID extends Number> {

    /**
     * Возвращает список всех сущностей.
     *
     * @return список объектов типа T.
     */
    List<T> getAll();

    /**
     * Ищет сущность по её идентификатору.
     *
     * @param id идентификатор сущности.
     * @return объект типа T, если сущность найдена.
     * @throws EntityNotFoundException если сущность с указанным идентификатором не найдена.
     */
    T getById(ID id);

    /**
     * Создает новую сущность.
     *
     * @param dto данные для создания сущности.
     * @return созданный объект типа T.
     */
    T create(T dto);

    /**
     * Обновляет существующую сущность.
     *
     * @param id идентификатор сущности.
     * @param dto данные для обновления сущности.
     * @return обновленный объект типа T.
     * @throws EntityNotFoundException если сущность с указанным идентификатором не найдена.
     */
    T update(ID id, T dto);

    /**
     * Удаляет сущность по её идентификатору.
     *
     * @param id идентификатор сущности.
     * @throws EntityNotFoundException если сущность с указанным идентификатором не найдена.
     */
    void delete(ID id);
}
