package com.arnor4eck.storage;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;

import java.util.Optional;

public interface SiteStatisticsStorage extends Storage<SiteStatistics> {
    void create(CreateSiteStatisticsRequest request);
    Optional<SiteStatistics> getLastStatisticsByMonitoringTaskId(long monitoringTaskId);
}
