import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.storage.in_memory.InMemorySiteStatisticsStorage;
import com.arnor4eck.util.request.CreateSiteStatisticsRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

public class SiteStatisticsStorage {

    private InMemorySiteStatisticsStorage storage;

    @BeforeEach
    public void setUp(){
        storage = new InMemorySiteStatisticsStorage();
    }

    @Test
    public void testGetLastStatisticsShouldReturnLatest(){
        CreateSiteStatisticsRequest notLatestRequest = new CreateSiteStatisticsRequest(
                LocalDateTime.now().minusDays(5), (short) 200,
                "10", true, 1);

        CreateSiteStatisticsRequest latestRequest = new CreateSiteStatisticsRequest(
                LocalDateTime.now(), (short) 200,
                "20", true, 1);

        storage.create(notLatestRequest);
        storage.create(latestRequest);

        Optional<SiteStatistics> latestStat = storage.getLastStatisticsByMonitoringTaskId(1);

        Assertions.assertTrue(latestStat.isPresent());
        Assertions.assertEquals(latestStat.get().getHash(), latestRequest.hash());
    }

    @Test
    public void testGetLastStatisticsShouldReturnEmptyOptional(){
        Optional<SiteStatistics> latestStat = storage.getLastStatisticsByMonitoringTaskId(1);

        Assertions.assertEquals(latestStat, Optional.empty());
    }

}
