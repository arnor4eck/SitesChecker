package com.arnor4eck.java_fx.components.task_component;

import com.arnor4eck.entity.SiteStatistics;
import javafx.beans.property.*;

public record SiteStatisticsFX(StringProperty checkTime,
                               IntegerProperty httpCode,
                               BooleanProperty isSameHash) {
    public SiteStatisticsFX(SiteStatistics stat){
        this(new SimpleStringProperty(stat.getCheckTime().toString()),
                new SimpleIntegerProperty(stat.getHttpCode()),
                new SimpleBooleanProperty(stat.isSameHash()));
    }
}
