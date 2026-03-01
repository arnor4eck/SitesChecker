package com.arnor4eck.java_fx.components;

import com.arnor4eck.SiteCheckerApplication;
import com.arnor4eck.SiteCheckers;
import com.arnor4eck.java_fx.ApplicationConstants;
import com.arnor4eck.java_fx.components.task_component.MonitoringTaskFX;
import com.arnor4eck.java_fx.components.task_component.ObservableMonitoringTaskStorage;
import com.arnor4eck.java_fx.utils.SplitPaneUtils;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public final class TasksComponent {

    private final Pane tasksPane;
    private final TableView<MonitoringTaskFX> tableView;

    @Getter(value = AccessLevel.NONE)
    private final ContextMenu contextMenu;

    public TasksComponent(SplitPaneUtils splitPaneUtils, Supplier<ObservableList<MonitoringTaskFX>> getAllTasks,
                          Consumer<Long> onDeleteTask, Consumer<Long> onCheckTask) {

        this.contextMenu = setUpContextMenu(onDeleteTask, onCheckTask);
        this.tableView = setUpTasksTableView(getAllTasks);
        this.tasksPane = setUpTasks(Objects.requireNonNull(splitPaneUtils));
    }

    private Pane setUpTasks(SplitPaneUtils splitPaneUtils){
        Pane pane = splitPaneUtils.createVBoxForSliding(5, 3);

        pane.setBackground(new Background(new BackgroundFill(ApplicationConstants.PRIMARY_COLOR, new CornerRadii(10), Insets.EMPTY)));

        pane.getChildren().add(this.tableView);

        return splitPaneUtils.createCoverPane(pane);
    }

    private ContextMenu setUpContextMenu(Consumer<Long> onDeleteTask,
                                         Consumer<Long> onCheckTask) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem checksItem = new MenuItem("Проверки");
        MenuItem deleteItem = new MenuItem("Удалить");

        deleteItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Удаление");
            alert.setContentText("Вы уверены, что хотит удалить этот компонент?");
            alert.setTitle("Удаление");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK)
                    onDeleteTask.accept(tableView.getSelectionModel().getSelectedItem().id().get());
            });
        });

        checksItem.setOnAction(event -> {
            onCheckTask.accept(tableView.getSelectionModel().getSelectedItem().id().get());
        });

        contextMenu.getItems().addAll(checksItem, deleteItem);

        return contextMenu;
    }

    @SuppressWarnings("unchecked")
    private TableView<MonitoringTaskFX> setUpTasksTableView(Supplier<ObservableList<MonitoringTaskFX>> getAllTasks){
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
        tableView.setItems(getAllTasks.get());

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
