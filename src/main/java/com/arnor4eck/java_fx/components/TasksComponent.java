package com.arnor4eck.java_fx.components;

import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.fx_component.MonitoringTaskFX;
import com.arnor4eck.fx_component.SiteStatisticsFX;
import com.arnor4eck.java_fx.utils.SplitPaneUtils;
import com.arnor4eck.java_fx.utils.TableViewBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public final class TasksComponent {

    private final Pane tasksPane;
    private final TableView<MonitoringTaskFX> tasksTableView;
    private TableView<SiteStatisticsFX> statisticsTableView;

    @Getter(value = AccessLevel.NONE)
    private final ContextMenu contextMenu;

    public TasksComponent(SplitPaneUtils splitPaneUtils, Supplier<ObservableList<MonitoringTaskFX>> getAllTasks,
                          Consumer<Long> onDeleteTask, Function<Long, ObservableList<SiteStatisticsFX>> onCheckTask) {

        this.contextMenu = setUpContextMenu(onDeleteTask, onCheckTask);
        this.tasksTableView = setUpTasksTableView(getAllTasks);
        this.tasksPane = setUpTasks(Objects.requireNonNull(splitPaneUtils));
        statisticsTableView = null;
    }

    public void setTasksTable(){
        setTable(tasksTableView);
    }

    private Pane setUpTasks(SplitPaneUtils splitPaneUtils){
        Pane pane = splitPaneUtils.createVBoxForSliding(5, 3);

        pane.setBackground(new Background(new BackgroundFill(ApplicationUtils.PRIMARY_COLOR, new CornerRadii(10), Insets.EMPTY)));

        pane.getChildren().add(this.tasksTableView);

        return splitPaneUtils.createCoverPane(pane);
    }

    private ContextMenu setUpContextMenu(Consumer<Long> onDeleteTask,
                                         Function<Long, ObservableList<SiteStatisticsFX>> onCheckTask) {
        ContextMenu contextMenu = new ContextMenu();

        contextMenu.getItems().addAll(
                setUpItem("Проверки",
                event -> {
                    if(statisticsTableView == null)
                        statisticsTableView = setUpStatisticsTableView();

                    statisticsTableView.setItems(onCheckTask.apply(
                            tasksTableView.getSelectionModel().getSelectedItem().id().get()
                    ));

                    setTable(statisticsTableView);
                }),
                setUpItem("Удалить",
                event -> {
                    ApplicationUtils.alert(Alert.AlertType.CONFIRMATION,
                                    "Удаление",
                                    "Вы уверены, что хотит удалить этот компонент?")
                    .showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK)
                            onDeleteTask.accept(tasksTableView.getSelectionModel().getSelectedItem().id().get());
                    });
                }));

        return contextMenu;
    }

    private MenuItem setUpItem(String name,
                               EventHandler<ActionEvent> handler){
        MenuItem item = new MenuItem(name);

        item.setOnAction(handler);

        return item;
    }

    private void setTable(TableView<?> tableView){
        ObservableList<Node> list = ((Pane) tasksPane.getChildren().getFirst()).getChildren();

        list.removeFirst();
        list.add(tableView);
    }

    private TableView<SiteStatisticsFX> setUpStatisticsTableView() {
        TableView<SiteStatisticsFX> statisticsTableView = TableViewBuilder.<SiteStatisticsFX>builder()
                .<String>addColumn("Время проверки", cellData -> cellData.getValue().checkTime())
                .<Number>addColumn("Код ответа", cellData -> cellData.getValue().httpCode())
                .<Boolean>addColumn("Изменение", cellData -> cellData.getValue().isSameHash())
                .build();

        statisticsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_NEXT_COLUMN);
        statisticsTableView.getStylesheets().add(getClass().getResource("/table.css").toExternalForm());

        return statisticsTableView;
    }

    private TableView<MonitoringTaskFX> setUpTasksTableView(Supplier<ObservableList<MonitoringTaskFX>> getAllTasks){
        TableView<MonitoringTaskFX> tableView = TableViewBuilder
                .<MonitoringTaskFX>builder()
                .<Number>addColumn("ID", cellData -> cellData.getValue().id())
                .<String>addColumn("Название",  cellData -> cellData.getValue().name())
                .<String>addColumn("Ссылка",  cellData -> cellData.getValue().url())
                .<Number>addColumn("Период", cellData -> cellData.getValue().period())
                .<String>addColumn("Единица времени", cellData -> cellData.getValue().unit())
                .build();

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_NEXT_COLUMN);
        tableView.getStylesheets().add(getClass().getResource("/table.css").toExternalForm());

        tableView.setItems(getAllTasks.get());

        tableView.setOnContextMenuRequested(event -> {
            MonitoringTaskFX selected = tableView.getSelectionModel().getSelectedItem();

            if (selected != null) {
                contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
            }
        });

        return tableView;
    }
}
