package com.arnor4eck;

import com.arnor4eck.storage.in_memory.InMemoryMonitoringTaskStorage;
import com.arnor4eck.storage.in_memory.InMemorySiteStatisticsStorage;

public class SiteCheckers {
    public static SiteCheckerApplication inMemory(){
        return new SiteCheckerApplication(new InMemoryMonitoringTaskStorage(),
                new InMemorySiteStatisticsStorage());
    }
}
