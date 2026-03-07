package com.arnor4eck;

import com.arnor4eck.storage.data_base.DataBase;
import com.arnor4eck.storage.data_base.DataBaseMonitoringTaskStorage;
import com.arnor4eck.storage.data_base.DataBaseSiteStatisticsStorage;
import com.arnor4eck.storage.in_memory.InMemoryMonitoringTaskStorage;
import com.arnor4eck.storage.in_memory.InMemorySiteStatisticsStorage;

public class SiteCheckers {

    private static class Handler{
        public static SiteCheckerApplication APP = SiteCheckers.inDB();
    }

    public static SiteCheckerApplication getInstance() {
        return Handler.APP;
    }

    private static SiteCheckerApplication inMemory(){
        return new SiteCheckerApplication(new InMemoryMonitoringTaskStorage(),
                new InMemorySiteStatisticsStorage());
    }

    private static SiteCheckerApplication inDB(){
        DataBase db = new DataBase("jdbc:sqlite:checker-app.db");
        Runtime.getRuntime().addShutdownHook(new Thread(db::close));

        return new SiteCheckerApplication(new DataBaseMonitoringTaskStorage(db),
                new DataBaseSiteStatisticsStorage(db));
    }
}
