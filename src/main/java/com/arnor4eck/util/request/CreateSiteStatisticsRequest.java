package com.arnor4eck.util.request;

import java.time.LocalDateTime;

/** Рекорд-класс для создания объекта статистики
 * @see com.arnor4eck.entity.SiteStatistics
 * */
public record CreateSiteStatisticsRequest(LocalDateTime checkTime,
                                          short httpCode,
                                          String hash,
                                          boolean isSameHash,
                                          long monitoringTaskId) {
}
