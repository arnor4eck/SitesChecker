package com.arnor4eck.util.request;

import java.time.temporal.ChronoUnit;

/** Рекорд-класс для создания объекта мониторинга сайта
 * @see com.arnor4eck.entity.MonitoringTask
 * */
public record CreateMonitoringTaskRequest(String name,
                                          String url,
                                          long period,
                                          ChronoUnit unit) {
}
