package com.arnor4eck;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.util.Logger;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/** Планировщик выполнения задач по мониторингу сайтов
 * */
public class TaskScheduler {

    /** Пул потоков для выполнения задач по мониторингу сайтов
     * */
    private final ExecutorService tasksExecutor;

    /** @see Logger
     * */
    private final Logger logger = Logger.getInstance();

    /** Пул потока для запуска TaskScheduler
     * */
    private final ExecutorService applicationExecutor;

    /** Работает ли TaskScheduler
     * */
    private final AtomicBoolean running;

    /** Хранилище задач по мониторингу
     * */
    private final MonitoringTaskStorage monitoringTaskStorage;

    /** Фабрика для создания Runnable для выполнения задач по мониторингу сайтов
     * */
    private final TaskRunnableFactory taskRunnableFactory;

    private final long TIME_TO_SLEEP = 500;

    public TaskScheduler(TaskRunnableFactory taskRunnableFactory,
                         MonitoringTaskStorage monitoringTaskStorage) {
        this.monitoringTaskStorage = monitoringTaskStorage;
        this.taskRunnableFactory = taskRunnableFactory;

        this.tasksExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
        this.applicationExecutor = Executors.newSingleThreadExecutor();

        this.running = new AtomicBoolean(false);
    }

    /** Старт планировщика
     * */
    public void start() {
        if(!running.compareAndSet(false, true))
            return;

        logger.info("Приложение запущено");

        applicationExecutor.submit(() -> {
            try {
                while (running.get()) {
                    monitoringTaskStorage.getAll()
                            .stream().filter(task ->
                                    task.getNextCheckTime().isBefore(LocalDateTime.now())
                            ).forEach(this::submitTask);

                    Thread.sleep(TIME_TO_SLEEP);
                }
            } catch (InterruptedException e) {
                running.set(false);
                Thread.currentThread().interrupt();
            }
        });
    }

    /** Выполнение задачи по мониторингу сайтов
     * */
    private void submitTask(MonitoringTask task){
        task.changeNextCheckTime();
        monitoringTaskStorage.updateExistingMonitoringTask(task);

        tasksExecutor.submit(taskRunnableFactory.create(task));
        logger.info("Сайт '%s' был проверен".formatted(task.getName()));
    }

    /** Остановка планировщика
     * */
    public void stop() {
        running.compareAndSet(true, false);
        logger.info("Приложение остановлено");
    }

    /** Работает ли приложение
     * @return boolean - Запущено ли приложение
     * */
    public boolean isRunning() {
        return running.get();
    }

    public void shutDown(){
        stop();

        logger.info("Завершение приложения...");
        if(!applicationExecutor.isShutdown())
            applicationExecutor.shutdown();

        if(!tasksExecutor.isShutdown()){
            tasksExecutor.shutdown();
            try {
                if(!tasksExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS)){
                    logger.warn("Не все задачи мониторинга были закончены");
                    tasksExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                tasksExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
