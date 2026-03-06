package com.arnor4eck.storage.data_base;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;

import java.sql.*;
import java.util.*;

public class DataBaseMonitoringTaskStorage extends AbstractDataBaseStorage<MonitoringTask> implements MonitoringTaskStorage {
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
                ApplicationUtils.parseStringToChronoUnit(rs.getString("unit")));
    }

    @Override
    public MonitoringTask create(CreateMonitoringTaskRequest request) {
        String statement = "INSERT INTO monitoring_task(url, name, period, unit, next_check_time) VALUES(?, ?, ?, ?, ?) RETURNING id";

        MonitoringTask monitoringTask = new MonitoringTask(-1, request.url(),
                request.name(), request.period(), request.unit());

        try(Connection con = this.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(statement)){
            preparedStatement.setString(1, monitoringTask.getUrl());
            preparedStatement.setString(2, monitoringTask.getName());
            preparedStatement.setLong(3, monitoringTask.getPeriod());
            preparedStatement.setString(4, ApplicationUtils.parseChronoUnitToString(monitoringTask.getUnit()));
            preparedStatement.setString(5, monitoringTask.getNextCheckTime().format(ApplicationUtils.formatter));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    monitoringTask.setId(resultSet.getLong("id"));
                    return monitoringTask;
                }
                throw new SQLException("Не удалось получить ID");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании задачи мониторинга", e);
        }
    }

    @Override
    public MonitoringTask updateExistingMonitoringTask(MonitoringTask monitoringTask) {
        return null;
    }

    @Override
    public Optional<MonitoringTask> getById(long id) {
        String statement = "SELECT * FROM monitoring_task WHERE id = ?";

        try(Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement(statement)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next())
                    return Optional.of(extract(rs));
            }

            return Optional.empty();
        }catch(SQLException e){
            throw new RuntimeException("Не найти задачу", e);
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
            throw new RuntimeException("Не удалить задачу", e);
        }
    }

    @Override
    public Collection<MonitoringTask> getAll() {
        String statement = "SELECT * FROM monitoring_task";

        List<MonitoringTask> monitoringTasks = new LinkedList<>();

        try(Connection con = this.getConnection();
            Statement st = con.createStatement()){

            try(ResultSet rs = st.executeQuery(statement)){
                while(rs.next())
                    monitoringTasks.add(extract(rs));
            }

            return monitoringTasks;
        } catch (Exception e) {
            throw new RuntimeException("Не удалось выполнить запрос", e);
        }
    }
}
