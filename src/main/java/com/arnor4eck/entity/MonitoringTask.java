package com.arnor4eck.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/** Сущность по мониторингу сайта
 * */
@Setter
@Getter
@ToString
public final class MonitoringTask {

    /** Уникальный идентификатор
     * */
    private long id;
    /** Время следующей проверки
     * */
    private LocalDateTime nextCheckTime;
    /** URL сайта
     * */
    private String url;
    /** Название мониторинга
     * */
    private String name;
    /** Период, через который необходимо проверять сайт
     * */
    private long period;
    /** Единица измерения @see MonitoringTask#period
     * */
    private ChronoUnit unit;

    public MonitoringTask(long id, String url, String name,
                          long period, ChronoUnit unit) {
        this.url = url;
        this.id = id;
        this.name = name;
        this.period = period;
        this.unit = unit;

        changeNextCheckTime();
    }

    /** Изменятет время следующей проверки сайта
     * */
    public void changeNextCheckTime(){
        this.nextCheckTime = LocalDateTime.now().plus(period, unit);
    }
}
