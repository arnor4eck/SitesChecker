package com.arnor4eck.storage.in_memory;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.storage.MonitoringTaskStorage;
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
