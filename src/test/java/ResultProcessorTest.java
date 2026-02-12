import com.arnor4eck.entity.SiteStatistics;
import com.arnor4eck.request_sender.HttpResponse;
import com.arnor4eck.statistics.ResultProcessor;
import com.arnor4eck.storage.SiteStatisticsStorage;
import com.arnor4eck.storage.in_memory.InMemorySiteStatisticsStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

public class ResultProcessorTest {

    private ResultProcessor resultProcessor;
    private SiteStatisticsStorage storage;

    @BeforeEach
    public void setUp() {
        storage = Mockito.spy(new InMemorySiteStatisticsStorage());
        resultProcessor = new ResultProcessor(storage);
    }

    @Test
    public void testShouldHasSameHash() {
        Mockito.when(storage.getLastStatisticsByMonitoringTaskId(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        HttpResponse response = new HttpResponse((short) 200, "12");

        resultProcessor.addStatistics(1, response);

        Assertions.assertTrue(storage.getById(1).isPresent());
        Assertions.assertTrue(storage.getById(1).get().isSameHash());
    }

    @Test
    public void testShouldNotHasSameHash() {
        SiteStatistics returnedStatistics = new SiteStatistics(1, LocalDateTime.now(), (short) 200, "11", true, 1);

        Mockito.when(storage.getLastStatisticsByMonitoringTaskId(Mockito.anyLong()))
                .thenReturn(Optional.of(returnedStatistics));

        HttpResponse response = new HttpResponse((short) 200, "12");

        resultProcessor.addStatistics(1, response);

        Assertions.assertTrue(storage.getById(1).isPresent());
        Assertions.assertFalse(storage.getById(1).get().isSameHash());
    }

}
