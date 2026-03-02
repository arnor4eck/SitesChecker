package com.arnor4eck.java_fx.components;

import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.fx_component.MonitoringTaskFX;
import com.arnor4eck.fx_component.SiteStatisticsFX;
import com.arnor4eck.java_fx.utils.SplitPaneUtils;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public class TaskSplitComponent {

    private final TasksComponent tasksComponent;
    private final LogsComponent logsComponent;
    private final SplitPane taskSplitPane;

    public TaskSplitComponent(SplitPaneUtils splitPaneUtils, Supplier<ObservableList<MonitoringTaskFX>> allTasks,
                              Consumer<Long> onDeleteTask, Function<Long, ObservableList<SiteStatisticsFX>> onCheckTask) {
        this.tasksComponent = new TasksComponent(splitPaneUtils, allTasks, onDeleteTask, onCheckTask);
        this.logsComponent = new LogsComponent(splitPaneUtils);

        this.taskSplitPane = setUpTasksPane();
    }

    private SplitPane setUpTasksPane(){
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setBackground(ApplicationUtils.BASE_BACKGROUND);
        splitPane.setPadding(ApplicationUtils.BASE_INSETS);
        splitPane.setDividerPositions(0.8);

        // Сайты
        Pane tasks = this.tasksComponent.getTasksPane();

        // Логи
        Pane logs = this.logsComponent.getLogsPane();

        splitPane.getItems().addAll(tasks, logs);

        return splitPane;
    }

}
