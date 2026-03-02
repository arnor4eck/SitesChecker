package com.arnor4eck.java_fx.components;

import com.arnor4eck.java_fx.ApplicationUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public final class BottomComponent {

    private final Label status;
    private final Pane bottomPane;

    public BottomComponent() {
        this.status = setUpStatus();
        this.bottomPane = setUpBottom();
    }

    private Label setUpStatus(){
        Label l = new Label("Статус: Ожидание");
        l.setStyle("-fx-text-fill: white;");

        return l;
    }

    private Pane setUpBottom(){
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(5));
        bottom.setBackground(ApplicationUtils.BASE_BACKGROUND);

        bottom.getChildren().addAll(this.status);

        return bottom;
    }
}
