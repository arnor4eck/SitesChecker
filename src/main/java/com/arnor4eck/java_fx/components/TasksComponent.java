package com.arnor4eck.java_fx.components;

import com.arnor4eck.java_fx.ApplicationConstants;
import com.arnor4eck.java_fx.components.task_component.MonitoringTaskFX;
import com.arnor4eck.java_fx.components.task_component.ObservableMonitoringTaskStorage;
import com.arnor4eck.java_fx.utils.SplitPaneUtils;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class TasksComponent {

    private final Pane tasksPane;
    private final TableView<MonitoringTaskFX> tableView;

    private MenuItem deleteItem;
    private MenuItem checksItem;

    @Getter(value = AccessLevel.NONE)
    private final SplitPaneUtils splitPaneUtils;
    @Getter(value = AccessLevel.NONE)
    private final ContextMenu contextMenu;

    private final ObservableMonitoringTaskStorage observableMonitoringTaskStorage;

    public TasksComponent(SplitPaneUtils splitPaneUtils,
                          ObservableMonitoringTaskStorage observableMonitoringTaskStorage) {
        this.splitPaneUtils = Objects.requireNonNull(splitPaneUtils);
        this.observableMonitoringTaskStorage = observableMonitoringTaskStorage;

        this.tableView = setUpTasksTableView();
        this.tasksPane = setUpTasks();
        this.contextMenu = setUpContextMenu();
    }

    private Pane setUpTasks(){
        Pane pane = splitPaneUtils.createVBoxForSliding(5, 3);

        pane.setBackground(new Background(new BackgroundFill(ApplicationConstants.PRIMARY_COLOR, new CornerRadii(10), Insets.EMPTY)));

        pane.getChildren().add(this.tableView);

        return splitPaneUtils.createCoverPane(pane);
    }

    private ContextMenu setUpContextMenu(){
        ContextMenu contextMenu = new ContextMenu();

        this.checksItem = new MenuItem("Проверки");
        this.deleteItem = new MenuItem("Удалить");

        contextMenu.getItems().addAll(checksItem, deleteItem);

        return contextMenu;
    }

    @SuppressWarnings("unchecked")
    private TableView<MonitoringTaskFX> setUpTasksTableView(){
        TableView<MonitoringTaskFX> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_NEXT_COLUMN);
        tableView.getStylesheets().add(getClass().getResource("/table.css").toExternalForm());

        TableColumn<MonitoringTaskFX, Number> idColumn = setUpColumn("ID",
                cellData -> cellData.getValue().id());

        TableColumn<MonitoringTaskFX, String> nameColumn = setUpColumn("Название",
                cellData -> cellData.getValue().name());

        TableColumn<MonitoringTaskFX, String> urlColumn = setUpColumn("Ссылка",
                cellData -> cellData.getValue().url());

        TableColumn<MonitoringTaskFX, Number> periodColumn = setUpColumn("Период",
                cellData -> cellData.getValue().period());

        TableColumn<MonitoringTaskFX, String> unitColumn = setUpColumn("Единица времени", cellData -> cellData.getValue().unit());

        tableView.getColumns().addAll(idColumn, nameColumn, urlColumn, periodColumn, unitColumn);
        tableView.setItems(observableMonitoringTaskStorage.getAll());

        tableView.setOnContextMenuRequested(event -> {
            MonitoringTaskFX selected = tableView.getSelectionModel().getSelectedItem();

            if (selected != null) {
                contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
            }
        });

        return tableView;
    }

    private <T> TableColumn<MonitoringTaskFX, T> setUpColumn(String label,
                                                             Callback<TableColumn.CellDataFeatures<MonitoringTaskFX, T>, ObservableValue<T>> getter){
        TableColumn<MonitoringTaskFX, T> column = new TableColumn<>(label);
        column.setCellValueFactory(getter);

        return column;
    }
}
