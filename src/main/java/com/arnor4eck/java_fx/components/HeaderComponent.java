package com.arnor4eck.java_fx.components;

import com.arnor4eck.java_fx.ApplicationUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import lombok.Getter;

import java.io.FileNotFoundException;

@Getter
public final class HeaderComponent {

    private final Pane headerPane;

    public HeaderComponent(EventHandler<ActionEvent> listButtonCallback,
                           EventHandler<ActionEvent> addButtonCallback,
                           EventHandler<ActionEvent> docButtonCallback,
                           EventHandler<ActionEvent> startButtonCallback) throws FileNotFoundException {
        this.headerPane = setUpHeader(listButtonCallback, addButtonCallback,
                docButtonCallback, startButtonCallback);
    }

    private Button createHeaderButton(String name,
                                      String path) throws FileNotFoundException {
        Button button = new Button(name);

        Image logo = new Image(ApplicationUtils.class.getResourceAsStream(path));
        ImageView logoView = new ImageView(logo);

        logoView.setFitWidth(40);
        logoView.setFitHeight(40);
        logoView.setPreserveRatio(true);

        button.setGraphic(logoView);
        button.setContentDisplay(ContentDisplay.TOP);

        button.setStyle(
                "-fx-background-color: #26282EFF;" +
                        "-fx-border-color: grey;" +
                        "-fx-border-radius: 2px;" +
                        "-fx-border-width: 1px;" +
                        "-fx-text-fill: white;"
        );

        return button;
    }

    private Pane setUpHeader(EventHandler<ActionEvent> listButtonCallback,
                             EventHandler<ActionEvent> addButtonCallback,
                             EventHandler<ActionEvent> docButtonCallback,
                             EventHandler<ActionEvent> startButtonCallback) throws FileNotFoundException {
        HBox header = new HBox(); // общий HBox для всего хедера

        header.setBackground(ApplicationUtils.BASE_BACKGROUND);
        header.setPadding(ApplicationUtils.BASE_INSETS);

        Button docButton = createHeaderButton("Документация",
                "/images/icon-book.png");
        docButton.setOnAction(docButtonCallback);

        Button addButton = createHeaderButton("Добавить",
                "/images/icon-plus.png");
        addButton.setOnAction(addButtonCallback);

        Button listButton = createHeaderButton("Список",
                "/images/icon-list.png");
        listButton.setOnAction(listButtonCallback);

        HBox leftGroup = new HBox(5, addButton, listButton, docButton); // левая группа кнопок
        leftGroup.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region(); // спейсер, чтобы занимал оставшееся пространство
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button startButton = createHeaderButton("Старт",
                "/images/icon-start.png");
        startButton.setOnAction(actionEvent -> {
            startButton.setText(
                    startButton.getText().equals("Старт") ? "Стоп" : "Старт"
            );
            startButtonCallback.handle(actionEvent);
        });

        HBox rightGroup = new HBox(startButton); // правая группа кнопок
        rightGroup.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(leftGroup, spacer, rightGroup);

        return header;
    }
}
