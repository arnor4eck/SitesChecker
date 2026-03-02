package com.arnor4eck;

import com.arnor4eck.storage.in_memory.InMemoryMonitoringTaskStorage;
import com.arnor4eck.storage.in_memory.InMemorySiteStatisticsStorage;

public class SiteCheckers {

    private static class Handler{
        public static SiteCheckerApplication APP = SiteCheckers.inMemory();
    }

    public static SiteCheckerApplication getInstance() {
        return Handler.APP;
    }

    private static SiteCheckerApplication inMemory(){
        return new SiteCheckerApplication(new InMemoryMonitoringTaskStorage(),
                new InMemorySiteStatisticsStorage());
    }
}
