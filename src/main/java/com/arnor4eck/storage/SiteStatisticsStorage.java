package com.arnor4eck.storage;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;

public interface SiteStatisticsStorage extends Storage<SiteStatistics> {
    SiteStatistics create(CreateSiteStatisticsRequest request);
}
