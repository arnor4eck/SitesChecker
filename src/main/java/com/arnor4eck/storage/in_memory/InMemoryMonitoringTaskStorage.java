package com.arnor4eck.storage.in_memory;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.util.exception.UpdatableTaskNotExist;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Класс для хранения объектов мониторинга сайтов в памяти
 * */
public class InMemoryMonitoringTaskStorage implements MonitoringTaskStorage {

    /** Параметр для инкрементации ID
     * */
    private final AtomicLong counter = new AtomicLong(0);

    /** Хранение объектов в памяти
     * */
    private final Map<Long, MonitoringTask> tasks = new ConcurrentHashMap<>();

    @Override
    public MonitoringTask create(CreateMonitoringTaskRequest request) {

        MonitoringTask newTask = new MonitoringTask(counter.addAndGet(1),
                request.url(),
                request.name(),
                request.period(),
                request.unit());

        tasks.put(newTask.getId(), newTask);

        return newTask;
    }

    @Override
    public MonitoringTask updateExistingMonitoringTask(MonitoringTask monitoringTask) {
        MonitoringTask existingTask = this.getById(monitoringTask.getId())
                .orElseThrow(() -> new UpdatableTaskNotExist(
                        String.format("Monitoring task with id '%d' does not exist", monitoringTask.getId())));

        tasks.put(existingTask.getId(), existingTask.clone());

        return existingTask;
    }

    @Override
    public Optional<MonitoringTask> getById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public void deleteById(long id) {
        tasks.remove(id);
    }

    @Override
    public Collection<MonitoringTask> getAll() {
        return new ArrayList<>(tasks.values());
    }
}
