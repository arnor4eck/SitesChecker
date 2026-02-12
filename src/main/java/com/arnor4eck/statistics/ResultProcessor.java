package com.arnor4eck.statistics;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.request_sender.HttpResponse;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;

import java.time.LocalDateTime;
import java.util.Optional;

/** Обработчик ответов от сайтов
 * */
public class ResultProcessor {

    /** Хранилище данных для статистики
     * */
    private final SiteStatisticsStorage statisticsStorage;

    public ResultProcessor(SiteStatisticsStorage statisticsStorage) {
        this.statisticsStorage = statisticsStorage;
    }

    /** Обрабатывает ответ от сайта и создает новую запись в статистике
     *
     * @param monitoringTaskId ID сайта, за которым осуществляется мониторинг
     * @param response Ответ от сайта
     * */
    public void addStatistics(long monitoringTaskId,
                              HttpResponse response){

        Optional<SiteStatistics> lastStatistics = statisticsStorage.getLastStatisticsByMonitoringTaskId(monitoringTaskId);
        boolean isSameHash = false;

        // Если хеши совпадают, или если последней записи нет, значит первая запись
        if(lastStatistics.isPresent() && lastStatistics.get().getHash().equals(response.hash())
            || lastStatistics.isEmpty())
            isSameHash = true;

        statisticsStorage.create(new CreateSiteStatisticsRequest(
                LocalDateTime.now(),
                response.httpCode(),
                response.hash(),
                isSameHash,
                monitoringTaskId
        ));
    }
}
