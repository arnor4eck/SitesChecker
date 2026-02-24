package com.arnor4eck.java_fx.components;

import com.arnor4eck.entity.MonitoringTask;
import com.arnor4eck.java_fx.ApplicationConstants;
import com.arnor4eck.java_fx.utils.SplitPaneUtils;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class TasksComponent {

    private final Pane tasksPane;
    private final TableView<MonitoringTask> tableView;

    @Getter(value = AccessLevel.NONE)
    private final SplitPaneUtils splitPaneUtils;

    public TasksComponent(SplitPaneUtils splitPaneUtils) {
        this.splitPaneUtils = Objects.requireNonNull(splitPaneUtils);

        this.tableView = setUpTasksTableView();
        this.tasksPane = setUpTasks();
    }

    private Pane setUpTasks(){
        Pane pane = splitPaneUtils.createVBoxForSliding(5, 3);

        pane.setBackground(new Background(new BackgroundFill(ApplicationConstants.PRIMARY_COLOR, new CornerRadii(10), Insets.EMPTY)));

        pane.getChildren().add(this.tableView);

        return splitPaneUtils.createCoverPane(pane);
    }

    private TableView<MonitoringTask> setUpTasksTableView(){
        TableView<MonitoringTask> tableView = new TableView<>();

        return  tableView;
    }
}
