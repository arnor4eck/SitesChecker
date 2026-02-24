package com.arnor4eck.java_fx.components;

import com.arnor4eck.java_fx.ApplicationConstants;
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
public final class Header {

    private final Button listButton;
    private final Button addButton;
    private final Button docButton;
    private final Button startButton;
    private final Pane headerPane;

    public Header() throws FileNotFoundException {
        this.listButton = createHeaderButton("Список",
                "/images/icon-list.png");
        this.addButton = createHeaderButton("Добавить",
                "/images/icon-plus.png");
        this.docButton = createHeaderButton("Документация",
                "/images/icon-book.png");
        this.startButton = createHeaderButton("Старт",
                "/images/icon-start.png");

        this.headerPane = setUpHeader();
    }

    private Button createHeaderButton(String name,
                                      String path) throws FileNotFoundException {
        Button button = new Button(name);

        Image logo = new Image(ApplicationConstants.class.getResourceAsStream(path));
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

    private Pane setUpHeader() throws FileNotFoundException {
        HBox header = new HBox(); // общий HBox для всего хедера

        header.setBackground(ApplicationConstants.BASE_BACKGROUND);
        header.setPadding(ApplicationConstants.BASE_INSETS);

        HBox leftGroup = new HBox(5, addButton, listButton, docButton); // левая группа кнопок
        leftGroup.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region(); // спейсер, чтобы занимал оставшееся пространство
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox rightGroup = new HBox(startButton); // правая группа кнопок
        rightGroup.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(leftGroup, spacer, rightGroup);

        return header;
    }
}
