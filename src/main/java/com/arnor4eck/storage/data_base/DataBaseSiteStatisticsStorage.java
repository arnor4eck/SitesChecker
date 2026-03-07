package com.arnor4eck.storage.data_base;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.util.Logger;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DataBaseSiteStatisticsStorage extends AbstractDataBaseStorage<SiteStatistics> implements SiteStatisticsStorage {
    public DataBaseSiteStatisticsStorage(DataBase db) {
        super(db);
    }

    private final Logger logger = Logger.getInstance();

    @Override
    public void create(CreateSiteStatisticsRequest request) {
        String query = "INSERT INTO site_statistics(check_time, http_code, hash, is_same_hash, monitoring_task_id) VALUES(?,?,?,?,?)";

        try{
            executeQuery(query, (ps) -> {
                    ps.setString(1, request.checkTime().format(ApplicationUtils.formatter));
                    ps.setShort(2, request.httpCode());
                    ps.setString(3, request.hash());
                    ps.setBoolean(4, request.isSameHash());
                    ps.setLong(5, request.monitoringTaskId());

                    return ps.executeUpdate();
            });
        }catch (SQLException e){
            logger.error("Не удалось записать статистику: %s".formatted(e.getMessage()));
        }
    }

    @Override
    public Optional<SiteStatistics> getLastStatisticsByMonitoringTaskId(long monitoringTaskId) {
        String query = "SELECT * FROM site_statistics WHERE monitoring_task_id = ? ORDER BY id DESC LIMIT 1;";

        try {
            return executeQuery(query, ps -> {
                ps.setLong(1, monitoringTaskId);

                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next())
                        return Optional.of(extract(rs));
                }
                return Optional.empty();
            });
        }catch(SQLException e){
            logger.error("Не удалось найти элементы статистики: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалось найти элементы статистики", e);
        }
    }

    @Override
    public Collection<SiteStatistics> getAllStatisticsByMonitoringTaskId(long monitoringTaskId) {
        String query = "SELECT * FROM site_statistics WHERE monitoring_task_id = ?";

        try{
            return executeQuery(query, ps -> {
                List<SiteStatistics> siteStatisticsList = new LinkedList<>();

                ps.setLong(1, monitoringTaskId);

                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next())
                        siteStatisticsList.add(extract(rs));
                }

                return siteStatisticsList;
            });
        } catch(SQLException e){
            logger.error("Не удалось найти элементы статистики: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалось найти элементы статистики", e);
        }
    }

    @Override
    public Optional<SiteStatistics> getById(long id) {
        String query = "SELECT * FROM site_statistics WHERE id = ?";

        try{
           return executeQuery(query, ps -> {
               ps.setLong(1, id);

               try(ResultSet rs = ps.executeQuery()){
                   if(rs.next())
                       return Optional.of(extract(rs));
               }

               return Optional.empty();
           });
        }catch(SQLException e){
            logger.error("Не удалось найти элемент статистики: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалось найти элемент статистики", e);
        }
    }

    @Override
    public void deleteById(long id) {
        String query = "DELETE FROM site_statistics WHERE id = ?";

        try{
            executeQuery(query, ps -> {
                ps.setLong(1, id);

                return ps.executeUpdate();
            });
        }catch(SQLException e){
            logger.error("Не удалить элемент статистики: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалить элемент статистики", e);
        }
    }

    @Override
    public Collection<SiteStatistics> getAll() {
        String query = "SELECT * FROM site_statistics";

        try{
            return Collections.unmodifiableCollection(executeQuery(query, ps -> {
                List<SiteStatistics> list = new LinkedList<>();

                try(ResultSet rs = ps.executeQuery(query)){
                    while(rs.next())
                        list.add(extract(rs));
                }

                return list;
            }));
        } catch (SQLException e) {
            logger.error("Не удалось загрузить все элементы статистики: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалось загрузить все элементы статистики", e);
        }
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
