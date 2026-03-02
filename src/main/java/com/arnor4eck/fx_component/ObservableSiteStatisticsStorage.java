package com.arnor4eck.fx_component;

import com.arnor4eck.storage.SiteStatisticsStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public final class ObservableSiteStatisticsStorage {

    private final SiteStatisticsStorage siteStatisticsStorage;

    public ObservableSiteStatisticsStorage(SiteStatisticsStorage siteStatisticsStorage) {
        this.siteStatisticsStorage = siteStatisticsStorage;
    }

    public ObservableList<SiteStatisticsFX> getAllByMonitoringTaskId(long monitoringTaskId) {
        return FXCollections.observableArrayList(siteStatisticsStorage.getAllStatisticsByMonitoringTaskId(monitoringTaskId)
                .stream()
                .map(SiteStatisticsFX::new).toList());
    }
}
