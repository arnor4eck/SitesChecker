package com.arnor4eck.storage.in_memory;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Класс для хранения объектов статистики в памяти
 * */
public class InMemorySiteStatisticsStorage implements SiteStatisticsStorage {

    /** Параметр для инкрементации ID
     * */
    private final AtomicLong counter = new AtomicLong(0);

    /** Хранение объектов в памяти
     * */
    private final Map<Long, SiteStatistics> statistics = new ConcurrentHashMap<>();

    @Override
    public void create(CreateSiteStatisticsRequest request) {
        SiteStatistics newStatistics = new SiteStatistics(counter.addAndGet(1),
                request.checkTime(),
                request.httpCode(),
                request.hash(),
                request.isSameHash(),
                request.monitoringTaskId());

        statistics.put(newStatistics.getId(), newStatistics);

    }

    @Override
    public Optional<SiteStatistics> getLastStatisticsByMonitoringTaskId(long monitoringTaskId) {
        return Optional.ofNullable(
                statistics.values().stream()
                        .filter(s -> s.getMonitoringTaskId() == monitoringTaskId)
                        .toList().getLast());
    }

    @Override
    public Optional<SiteStatistics> getById(long id) {
        return Optional.ofNullable(statistics.get(id));
    }

    @Override
    public void deleteById(long id) {
        statistics.remove(id);
    }

    @Override
    public Collection<SiteStatistics> getAll() {
        return new ArrayList<>(statistics.values());
    }
}
