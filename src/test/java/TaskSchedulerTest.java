import com.arnor4eck.TaskRunnableFactory;
import com.arnor4eck.TaskScheduler;
import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.storage.MonitoringTaskStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;


class TaskSchedulerTest {

    private TaskScheduler taskScheduler;
    private MonitoringTaskStorage storage;

    @BeforeEach
    public void setup() {
        TaskRunnableFactory factory = Mockito.mock(TaskRunnableFactory.class);
        storage = Mockito.mock(MonitoringTaskStorage.class);

        taskScheduler = new TaskScheduler(factory, storage);
    }

    @AfterEach
    public void teardown() {
        taskScheduler.shutDown();
        taskScheduler = null;
    }

    @Test
    public void testStartShouldReturnTrue(){
        taskScheduler.start();

        Assertions.assertTrue(taskScheduler.isRunning());
    }

    @Test
    public void testStopShouldReturnFalse(){
        taskScheduler.start();

        Assertions.assertTrue(taskScheduler.isRunning());

        taskScheduler.stop();

        Assertions.assertFalse(taskScheduler.isRunning());
    }

    @Test
    public void testSubmitTaskShouldVerifyChangeNextCheckTime() throws InterruptedException {
        MonitoringTask task = Mockito.mock(MonitoringTask.class);
        Mockito.when(task.getNextCheckTime()).thenReturn(LocalDateTime.MIN);

        Mockito.when(storage.getAll()).thenReturn(Collections.singletonList(task));

        taskScheduler.start();
        Thread.sleep(700); // чтоб планировщик успел выполнить

        Mockito.verify(task, Mockito.times(1)).changeNextCheckTime();
        Mockito.verify(storage, Mockito.atLeastOnce()).updateExistingMonitoringTask(task);
    }

    @Test
    public void testSubmitTaskShouldNoVerifyChangeNextCheckTime() throws InterruptedException {
        MonitoringTask task = Mockito.mock(MonitoringTask.class);
        Mockito.when(task.getNextCheckTime()).thenReturn(LocalDateTime.MAX);

        Mockito.when(storage.getAll()).thenReturn(Collections.singletonList(task));

        taskScheduler.start();
        Thread.sleep(700); // чтоб планировщик успел выполнить

        Mockito.verify(task, Mockito.times(0)).changeNextCheckTime();
    }
}
