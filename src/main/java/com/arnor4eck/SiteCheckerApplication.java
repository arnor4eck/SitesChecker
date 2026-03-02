package com.arnor4eck;

import com.arnor4eck.fx_component.MonitoringTaskFX;
import com.arnor4eck.fx_component.ObservableMonitoringTaskStorage;
import com.arnor4eck.fx_component.ObservableSiteStatisticsStorage;
import com.arnor4eck.fx_component.SiteStatisticsFX;
import com.arnor4eck.request_sender.OkHttpRequestSender;
import com.arnor4eck.statistics.ResultProcessor;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/** Основной класс приложения
 * */
public class SiteCheckerApplication {
    private final TaskScheduler scheduler;

    private final ObservableMonitoringTaskStorage monitoringTaskStorage;

    private final ObservableSiteStatisticsStorage observableSiteStatisticsStorage;

    public SiteCheckerApplication(MonitoringTaskStorage monitoringTaskStorage,
                                  SiteStatisticsStorage siteStatisticsStorage) {
        this.scheduler = new TaskScheduler(
                new TaskRunnableFactory(new ResultProcessor(siteStatisticsStorage),
                        new OkHttpRequestSender()),
                monitoringTaskStorage);
        this.monitoringTaskStorage = new ObservableMonitoringTaskStorage(monitoringTaskStorage);
        this.observableSiteStatisticsStorage = new ObservableSiteStatisticsStorage(siteStatisticsStorage);
    }

    public void start() {
        scheduler.start();
    }

    public boolean isRunning() {
        return scheduler.isRunning();
    }

    public void stop() {
        scheduler.stop();
    }

    public void addTask(CreateMonitoringTaskRequest request){
        monitoringTaskStorage.create(request);
    }

    public void removeTask(long taskId){
        monitoringTaskStorage.deleteById(taskId);
    }

    public ObservableList<MonitoringTaskFX> getALlTasks(){
        return FXCollections.unmodifiableObservableList(monitoringTaskStorage.getAll());
    }

    public ObservableList<SiteStatisticsFX> getAllSiteStatisticsByMonitoringTask(long taskId){
        return FXCollections.unmodifiableObservableList(observableSiteStatisticsStorage.getAllByMonitoringTaskId(taskId));
    }
}
