package com.arnor4eck.storage.data_base;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;

import java.sql.*;
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
        String query = "SELECT * FROM site_statistics WHERE monitoring_task_id = ?";
        List<SiteStatistics> siteStatisticsList = new LinkedList<>();

        try(Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            ps.setLong(1, monitoringTaskId);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next())
                    siteStatisticsList.add(extract(rs));
            }
            return siteStatisticsList;
        }catch(SQLException e){
            throw new RuntimeException("Не найти элементы статистики", e);
        }
    }

    @Override
    public Optional<SiteStatistics> getById(long id) {
        String statement = "SELECT * FROM site_statistics WHERE id = ?";

        try(Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement(statement)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next())
                    return Optional.of(extract(rs));
            }

            return Optional.empty();
        }catch(SQLException e){
            throw new RuntimeException("Не найти элемент статистики", e);
        }
    }

    @Override
    public void deleteById(long id) {
        String statement = "DELETE FROM site_statistics WHERE id = ?";

        try(Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement(statement)){
            ps.setLong(1, id);

            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException("Не удалить элемент статистики", e);
        }
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
