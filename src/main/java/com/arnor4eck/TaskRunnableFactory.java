package com.arnor4eck;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.request_sender.HttpResponse;
import com.arnor4eck.request_sender.RequestSender;
import com.arnor4eck.statistics.ResultProcessor;

public class TaskRunnableFactory {

    private final ResultProcessor resultProcessor;
    private final RequestSender requestSender;

    public static class TaskRunnable implements Runnable {
        private final ResultProcessor resultProcessor;
        private final RequestSender requestSender;
        private final MonitoringTask task;

        public TaskRunnable(ResultProcessor resultProcessor,
                            RequestSender requestSender,
                            MonitoringTask task) {
            this.resultProcessor = resultProcessor;
            this.requestSender = requestSender;
            this.task = task;
        }

        @Override
        public void run() {
            HttpResponse r = requestSender.sendRequest(task.getUrl());

            resultProcessor.addStatistics(task.getId(), r);
        }
    }

    public TaskRunnableFactory(ResultProcessor resultProcessor,
                               RequestSender requestSender) {
        this.resultProcessor = resultProcessor;
        this.requestSender = requestSender;
    }

    public TaskRunnable create(MonitoringTask task) {
        return new TaskRunnable(resultProcessor, requestSender, task);
    }

}
