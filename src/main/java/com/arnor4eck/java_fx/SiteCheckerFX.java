package com.arnor4eck.java_fx;

import com.arnor4eck.SiteCheckerApplication;
import com.arnor4eck.SiteCheckers;
import com.arnor4eck.java_fx.components.BottomComponent;
import com.arnor4eck.java_fx.components.create_task_component.CreateMonitoringTaskComponent;
import com.arnor4eck.java_fx.components.HeaderComponent;
import com.arnor4eck.java_fx.components.TaskSplitComponent;
import com.arnor4eck.java_fx.utils.SplitPaneUtils;
import com.arnor4eck.util.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SiteCheckerFX extends Application {

    private final SiteCheckerApplication app = SiteCheckers.getInstance();

    private final BorderPane mainPane = new BorderPane();
    private final HeaderComponent headerComponent;
    private final TaskSplitComponent taskSplitComponent;
    private final BottomComponent bottomComponent;
    private final CreateMonitoringTaskComponent createMonitoringTaskComponent;

    public SiteCheckerFX() throws FileNotFoundException {
        SplitPaneUtils splitPaneUtils = new SplitPaneUtils();

        this.bottomComponent = new BottomComponent();
        this.createMonitoringTaskComponent = new CreateMonitoringTaskComponent(app::addTask);

        this.taskSplitComponent = new TaskSplitComponent(
                splitPaneUtils,
                app::getALlTasks,
                app::removeTask,
                app::getAllSiteStatisticsByMonitoringTask);

        this.headerComponent = new HeaderComponent(
                event -> {
                    mainPane.setCenter(this.taskSplitComponent.getTaskSplitPane());
                    taskSplitComponent.getTasksComponent().setTasksTable();
                },event -> {
                    mainPane.setCenter(this.createMonitoringTaskComponent.getCreateMonitoringTaskPane());
                },
                event -> {
                    try {
                        InputStream in = getClass().getResourceAsStream("/doc.html");
                        File tempFile = File.createTempFile("doc", ".html");
                        tempFile.deleteOnExit();

                        Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        if (Desktop.isDesktopSupported())
                            Desktop.getDesktop().open(tempFile);

                    } catch (Exception e) {
                        Logger.getInstance().error("Ошибка открытия документации: %s".formatted(e.getMessage()));
                    }
                },
                event -> {
                    if(app.isRunning()){
                        app.stop();
                        this.bottomComponent.getStatus().setText("Статус: Ожидание");
                    }else{
                        app.start();
                        this.bottomComponent.getStatus().setText("Статус: Запущено");
                    }
                });
    }

    private void prepareMainStage(Stage stage){
        stage.getIcons().add(ApplicationUtils.LOGO);

        stage.setTitle("Site Checker");

        stage.setMinWidth(500);
        stage.setMinHeight(500);
        stage.setWidth(600);
        stage.setHeight(600);

        stage.show();
    }

    @Override
    public void start(Stage stage) {
        mainPane.setTop(headerComponent.getHeaderPane());
        mainPane.setCenter(taskSplitComponent.getTaskSplitPane());
        mainPane.setBottom(bottomComponent.getBottomPane());

        BorderPane root = new BorderPane();
        root.setCenter(mainPane);

        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        prepareMainStage(stage);
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            event.consume();

            new Thread(() -> {
                app.stop();

                Platform.runLater(stage::close);
            }).start();
        });

        Logger.getInstance().info("Приложение готово к работе");
    }
}
