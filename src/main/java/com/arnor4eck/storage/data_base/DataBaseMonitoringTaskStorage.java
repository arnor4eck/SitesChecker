package com.arnor4eck.storage.data_base;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.util.Logger;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DataBaseMonitoringTaskStorage extends AbstractDataBaseStorage<MonitoringTask> implements MonitoringTaskStorage {

    private final Logger logger = Logger.getInstance();

    public DataBaseMonitoringTaskStorage(DataBase db) {
        super(db);
    }

    @Override
    protected MonitoringTask extract(ResultSet rs) throws SQLException {
        return new MonitoringTask(
                rs.getLong("id"),
                rs.getString("url"),
                rs.getString("name"),
                rs.getLong("period"),
                ApplicationUtils.parseStringToChronoUnit(rs.getString("unit")),
                LocalDateTime.parse(rs.getString("next_check_time"), ApplicationUtils.formatter));
    }

    @Override
    public MonitoringTask create(CreateMonitoringTaskRequest request) {
        String query = "INSERT INTO monitoring_task(url, name, period, unit, next_check_time) VALUES(?, ?, ?, ?, ?) RETURNING id";

        try{
           return executeQuery(query, ps -> {
                MonitoringTask monitoringTask = new MonitoringTask(-1, request.url(),
                        request.name(), request.period(), request.unit());

                ps.setString(1, monitoringTask.getUrl());
                ps.setString(2, monitoringTask.getName());
                ps.setLong(3, monitoringTask.getPeriod());
                ps.setString(4, ApplicationUtils.parseChronoUnitToString(monitoringTask.getUnit()));
                ps.setString(5, monitoringTask.getNextCheckTime().format(ApplicationUtils.formatter));

                try(ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        monitoringTask.setId(resultSet.getLong("id"));
                        return monitoringTask;
                    }
                    throw new SQLException("Не удалось получить ID");
                }
            });
        } catch (Exception e) {
            logger.error("Ошибка при создании задачи мониторинга: %s".formatted(e.getMessage()));
            throw new RuntimeException("Ошибка при создании задачи мониторинга", e);
        }
    }

    @Override
    public MonitoringTask updateExistingMonitoringTask(MonitoringTask monitoringTask) {
        String query = "UPDATE monitoring_task SET url = ?, name = ?, period = ?, unit = ?, next_check_time = ? WHERE id = ?";

        try{
            return executeQuery(query, ps -> {
                ps.setString(1, monitoringTask.getUrl());
                ps.setString(2, monitoringTask.getName());
                ps.setLong(3, monitoringTask.getPeriod());
                ps.setString(4, ApplicationUtils.parseChronoUnitToString(monitoringTask.getUnit()));
                ps.setString(5, monitoringTask.getNextCheckTime().format(ApplicationUtils.formatter));
                ps.setLong(6, monitoringTask.getId());

                ps.executeUpdate();

                return monitoringTask;
            });
        }catch(SQLException e){
            logger.error("Не удалось обновить задачу: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалось обновить задачу", e);
        }
    }

    @Override
    public Optional<MonitoringTask> getById(long id) {
        String query = "SELECT * FROM monitoring_task WHERE id = ?";

        try{
            return executeQuery(query, ps -> {
                ps.setLong(1, id);

                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next())
                        return Optional.of(extract(rs));
                }
                return Optional.empty();
            });
        } catch(SQLException e){
            logger.error("Не удалось найти задачу: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалось найти задачу", e);
        }
    }

    @Override
    public void deleteById(long id) {
        String statement = "DELETE FROM monitoring_task WHERE id = ?";

        try(Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement(statement)){
            ps.setLong(1, id);

            ps.executeUpdate();
        }catch(SQLException e){
            logger.error("Не удалось удалить задачу: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалось удалить задачу", e);
        }
    }

    @Override
    public Collection<MonitoringTask> getAll() {
        String query = "SELECT * FROM monitoring_task";

        try{
            return executeQuery(query, ps -> {
                List<MonitoringTask> monitoringTasks = new LinkedList<>();

                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next())
                        monitoringTasks.add(extract(rs));
                }

                return monitoringTasks;
            });
        } catch (Exception e) {
            logger.error("Не удалось найти задачи: %s".formatted(e.getMessage()));
            throw new RuntimeException("Не удалось найти задачи", e);
        }
    }
}
