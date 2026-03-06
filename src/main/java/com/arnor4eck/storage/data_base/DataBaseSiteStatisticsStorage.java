package com.arnor4eck.storage.data_base;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

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
        String query = "SELECT * FROM site_statistics";
        List<SiteStatistics> list = new LinkedList<>();

        try(Connection con = this.getConnection();
            Statement st = con.createStatement()){

            try(ResultSet rs = st.executeQuery(query)){
                while(rs.next())
                    list.add(extract(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось загрузить все записи", e);
        }

        return Collections.unmodifiableCollection(list);
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
