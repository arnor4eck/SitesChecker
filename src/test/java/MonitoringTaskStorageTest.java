import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.storage.MonitoringTaskStorage;
import com.arnor4eck.storage.in_memory.InMemoryMonitoringTaskStorage;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;

public class MonitoringTaskStorageTest {

    private MonitoringTaskStorage monitoringTaskStorage;

    @BeforeEach
    public void setUp(){
        monitoringTaskStorage = new InMemoryMonitoringTaskStorage();
    }

    @Test
    public void testShouldSaveEntity(){
        CreateMonitoringTaskRequest request =
                new CreateMonitoringTaskRequest("test monitoring task",
                        "https://square.github.io/okhttp/",
                        5, ChronoUnit.HOURS);

        monitoringTaskStorage.create(request);

        MonitoringTask savedTask = monitoringTaskStorage.getById(1).get();

        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(savedTask.getName(), request.name());
    }

    @Test
    public void testShouldDeleteEntity(){
        CreateMonitoringTaskRequest request =
                new CreateMonitoringTaskRequest("test monitoring task",
                        "https://square.github.io/okhttp/",
                        5, ChronoUnit.HOURS);

        monitoringTaskStorage.create(request);

        MonitoringTask savedTask = monitoringTaskStorage.getById(1).get();

        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(savedTask.getName(), request.name());

        monitoringTaskStorage.deleteById(1);

        Optional<MonitoringTask> deletedTask = monitoringTaskStorage.getById(1);

        Assertions.assertEquals(deletedTask, Optional.empty());
    }

    @Test
    public void testShouldReturnCollection(){
        final int SIZE = 5;

        for(int i = 0; i < SIZE; i++){
            monitoringTaskStorage.create(
                    new CreateMonitoringTaskRequest("test monitoring task",
                            "https://square.github.io/okhttp/",
                            5, ChronoUnit.HOURS));
        }

        Collection<MonitoringTask> collection = monitoringTaskStorage.getAll();

        Assertions.assertEquals(SIZE, collection.size());
    }

    @Test
    public void testShouldReturnEmptyCollection(){
        monitoringTaskStorage.create(
                new CreateMonitoringTaskRequest("test monitoring task",
                        "https://square.github.io/okhttp/",
                        5, ChronoUnit.HOURS));

        MonitoringTask savedTask = monitoringTaskStorage.getById(1).get();

        savedTask.setName("new test monitoring task");

        monitoringTaskStorage.updateExistingMonitoringTask(savedTask);

        MonitoringTask updatedMonitoringTask = monitoringTaskStorage.getById(1).get();

        Assertions.assertNotNull(updatedMonitoringTask);
        Assertions.assertEquals(updatedMonitoringTask.getName(), savedTask.getName());
    }
}
