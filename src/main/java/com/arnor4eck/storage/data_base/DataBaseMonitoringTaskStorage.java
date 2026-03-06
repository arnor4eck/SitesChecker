package com.arnor4eck.storage.data_base;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DataBaseMonitoringTaskStorage extends AbstractDataBaseStorage implements MonitoringTaskStorage {
    public DataBaseMonitoringTaskStorage(DataBase db) {
        super(db);
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
        return Optional.empty();
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public Collection<MonitoringTask> getAll() {
        return List.of();
    }
}
