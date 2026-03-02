package com.arnor4eck.java_fx.components;

import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.java_fx.utils.SplitPaneUtils;
import com.arnor4eck.util.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class LogsComponent {

    private final Pane logsPane;
    private final TextArea logsArea;

    public LogsComponent(SplitPaneUtils splitPaneUtils) {
        this.logsArea = setUpTextArea();
        this.logsPane = setUpLogs(Objects.requireNonNull(splitPaneUtils));
    }

    private Pane setUpLogs(SplitPaneUtils splitPaneUtils){
        Pane pane = splitPaneUtils.createVBoxForSliding(5, 3, logsArea);

        pane.setBackground(new Background(
                new BackgroundFill(ApplicationUtils.LOG_COLOR, new CornerRadii(10), Insets.EMPTY)
        ));

        VBox.setVgrow(logsArea, Priority.ALWAYS);

        return splitPaneUtils.createCoverPane(pane);
    }

    private TextArea setUpTextArea(){
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);

        logArea.setMinHeight(0);  // убираем минимальную высоту
        logArea.setPrefHeight(Region.USE_COMPUTED_SIZE);  // используем вычисляемый размер
        logArea.setMaxHeight(Double.MAX_VALUE);  // убираем максимальную высоту

        logArea.setMinWidth(0);
        logArea.setPrefWidth(Region.USE_COMPUTED_SIZE);
        logArea.setMaxWidth(Double.MAX_VALUE);

        Logger.getInstance().setLogger(logArea::appendText);

        // автоматическая прокрутка вниз
        logArea.textProperty().addListener((obs, old, newVal) -> {
            logArea.setScrollTop(Double.MAX_VALUE);
        });

        logArea.getStyleClass().add("log-area");

        return logArea;
    }

}
