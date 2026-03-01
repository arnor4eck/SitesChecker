package com.arnor4eck.java_fx.components.task_component;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public final class ObservableMonitoringTaskStorage {

    private final MonitoringTaskStorage storage;
    private final ObservableList<MonitoringTaskFX> monitoringTasks;

    public ObservableMonitoringTaskStorage(MonitoringTaskStorage storage) {
        this.storage = storage;
        this.monitoringTasks = FXCollections.observableArrayList();
        monitoringTasks.addAll(storage.getAll().stream().map(MonitoringTaskFX::new).toList());
    }

    public boolean create(CreateMonitoringTaskRequest request) {
        return monitoringTasks.add(
                new MonitoringTaskFX(storage.create(request))
        );
    }

    public ObservableList<MonitoringTaskFX> getAll() {
        return monitoringTasks;
    }

    public void deleteById(long id) {
        storage.deleteById(id);
        monitoringTasks.removeIf(t -> t.id().get() == id);
    }
}
