package com.arnor4eck.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Статистика посещённого сайта
 */
@Getter
@Setter
@AllArgsConstructor
public class SiteStatistics{
    /** Уникальный идентификатор записи
     */
    private long id;
    /** Время проверки сайта
     * */
    private LocalDateTime checkTime;
    /** HTTP-код ответа
     * */
    private short httpCode;
    /** Хэш содержимого страницы
     * */
    private String hash;
    /** Совпадает ли хэш с предыдущей проверкой
     * */
    private boolean isSameHash;
    /** ID задачи мониторинга
     * */
    private long monitoringTaskId;
}
