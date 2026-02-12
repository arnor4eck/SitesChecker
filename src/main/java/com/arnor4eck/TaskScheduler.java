package com.arnor4eck;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.storage.MonitoringTaskStorage;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/** Планировщик выполнения задач по мониторингу сайтов
 * */
public class TaskScheduler {

    /** Пул потоков для выполнения задач по мониторингу сайтов
     * */
    private ExecutorService tasksExecutor;

    /** Пул потока для запуска TaskScheduler
     * */
    private ExecutorService applicationExecutor;

    /** Работает ли TaskScheduler
     * */
    private final AtomicBoolean running;

    /** Хранилище задач по мониторингу
     * */
    private final MonitoringTaskStorage monitoringTaskStorage;

    /** Фабрика для создания Runnable для выполнения задач по мониторингу сайтов
     * */
    private final TaskRunnableFactory taskRunnableFactory;

    public TaskScheduler(TaskRunnableFactory taskRunnableFactory,
                         MonitoringTaskStorage monitoringTaskStorage) {
        this.monitoringTaskStorage = monitoringTaskStorage;
        this.taskRunnableFactory = taskRunnableFactory;

        this.running = new AtomicBoolean(false);
    }

    /** Старт планировщика
     * */
    public void start() {
        if(!running.compareAndSet(false, true))
            return;

        // при каждом новом запуске пересоздаем пулы
        this.tasksExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
        this.applicationExecutor = Executors.newSingleThreadExecutor();

        applicationExecutor.submit(() -> {
            try {
                while (running.get()) {
                    monitoringTaskStorage.getAll()
                            .stream().filter(task ->
                                    task.getNextCheckTime().isBefore(LocalDateTime.now())
                            ).forEach(this::submitTask);

                    Thread.sleep(100);
                    System.out.println(running.get());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /** Выполнение задачи по мониторингу сайтов
     * */
    private void submitTask(MonitoringTask task){
        task.changeNextCheckTime();
        monitoringTaskStorage.updateExistingMonitoringTask(task);

        tasksExecutor.submit(taskRunnableFactory.create(task));
    }

    /** Остановка планировщика
     * */
    public void stop() {
        if (running.compareAndSet(true, false)) {
            tasksExecutor.close();
            tasksExecutor = null;

            applicationExecutor.close();
            applicationExecutor = null;
        }
    }
}
