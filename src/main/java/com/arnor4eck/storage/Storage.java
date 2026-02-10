package com.arnor4eck.storage;

import java.util.Collection;
import java.util.Optional;

/** Интерфейс для взаимодействия с данными
 * */
public interface Storage<T> {
    /** Найти элемент по ID
     *
     * @param id Уникальный идентификатор
     * @return Объект, с уникальным идентификатором id
     * */
    Optional<T> getById(long id);

    /** Удалить элемент по ID
     *
     * @param id Уникальный идентификатор
     * */
    void deleteById(long id);

    /** Найти все элементы
     * @return Все объекты
     * */
    Collection<T> getAll();
}
