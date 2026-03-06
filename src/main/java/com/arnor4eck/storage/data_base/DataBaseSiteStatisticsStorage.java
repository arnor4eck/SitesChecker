package com.arnor4eck.storage.data_base;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DataBaseSiteStatisticsStorage extends AbstractDataBaseStorage<SiteStatistics> implements SiteStatisticsStorage {
    public DataBaseSiteStatisticsStorage(DataBase db) {
        super(db);
    }

    @Override
    public void create(CreateSiteStatisticsRequest request) {

    }

    @Override
    public Optional<SiteStatistics> getLastStatisticsByMonitoringTaskId(long monitoringTaskId) {
        return Optional.empty();
    }

    @Override
    public Collection<SiteStatistics> getAllStatisticsByMonitoringTaskId(long monitoringTaskId) {
        return List.of();
    }

    @Override
    public Optional<SiteStatistics> getById(long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public Collection<SiteStatistics> getAll() {
        return List.of();
    }

    @Override
    protected SiteStatistics extract(ResultSet rs) throws SQLException {
        return new SiteStatistics(rs.getLong("id"),
                LocalDateTime.parse(rs.getString("check_time"), ApplicationUtils.formatter),
                rs.getShort("http_code"),
                rs.getString("hash"),
                rs.getBoolean("is_same_hash"),
                rs.getLong("monitoring_task_id"));
    }
}
