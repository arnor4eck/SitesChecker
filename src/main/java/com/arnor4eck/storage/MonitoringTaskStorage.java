package com.arnor4eck.storage;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;

public interface MonitoringTaskStorage extends Storage<MonitoringTask> {
    MonitoringTask create(CreateMonitoringTaskRequest request);
}
