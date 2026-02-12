package com.arnor4eck;

import com.arnor4eck.request_sender.OkHttpRequestSender;
import com.arnor4eck.statistics.ResultProcessor;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;

/** Основной класс приложения
 * */
public class SiteCheckerApplication {
    private final TaskScheduler scheduler;

    private final MonitoringTaskStorage monitoringTaskStorage;

    public SiteCheckerApplication(MonitoringTaskStorage monitoringTaskStorage,
                                  SiteStatisticsStorage siteStatisticsStorage) {
        this.scheduler = new TaskScheduler(
                new TaskRunnableFactory(new ResultProcessor(siteStatisticsStorage),
                        new OkHttpRequestSender()),
                monitoringTaskStorage);
        this.monitoringTaskStorage =  monitoringTaskStorage;
    }

    public void start() {
        scheduler.start();
    }

    public void stop() {
        scheduler.stop();
    }

    public void addTask(CreateMonitoringTaskRequest request){
        monitoringTaskStorage.create(request);
    }
}
