package com.arnor4eck.fx_component;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.java_fx.ApplicationUtils;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.temporal.ChronoUnit;

public record MonitoringTaskFX(LongProperty id,
                       StringProperty name,
                       StringProperty url,
                       LongProperty period,
                       StringProperty unit) {
    public MonitoringTaskFX(MonitoringTask task) {
        this(
            new SimpleLongProperty(task.getId()),
            new SimpleStringProperty(task.getName()),
            new SimpleStringProperty(task.getUrl()),
            new SimpleLongProperty(task.getPeriod()),
            new SimpleStringProperty(ApplicationUtils.parseChronoUnitToString(task.getUnit()))
        );
    }
}