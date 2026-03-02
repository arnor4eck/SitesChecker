package com.arnor4eck.fx_component;

import com.arnor4eck.entity.MonitoringTask;
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
            new SimpleStringProperty(parseChronoUnitToString(task.getUnit()))
        );
    }

    private static String parseChronoUnitToString(ChronoUnit value) {
        return switch (value) {
            case SECONDS -> "Секунда";
            case MINUTES -> "Минута";
            case HOURS -> "Час";
            default -> throw new IllegalArgumentException("Unsupported unit: " + value);
        };
    }
}