package com.arnor4eck.fx_component;

import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.java_fx.ApplicationUtils;
import javafx.beans.property.*;

public record SiteStatisticsFX(StringProperty checkTime,
                               IntegerProperty httpCode,
                               BooleanProperty isSameHash) {
    public SiteStatisticsFX(SiteStatistics stat){
        this(new SimpleStringProperty(stat.getCheckTime().format(ApplicationUtils.formatter)),
                new SimpleIntegerProperty(stat.getHttpCode()),
                new SimpleBooleanProperty(!stat.isSameHash()));
    }
}
