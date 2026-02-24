package com.arnor4eck.java_fx.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public final class SplitPaneUtils {

    public Pane createCoverPane(Pane pane){
        // перекрываем vbox, чтобы splitpane мог спокойно слайдиться
        Pane cover = new Pane(pane);
        cover.setBackground(pane.getBackground());

        pane.prefWidthProperty().bind(cover.widthProperty());
        pane.prefHeightProperty().bind(cover.heightProperty());
        pane.maxWidthProperty().bind(cover.widthProperty());
        pane.maxHeightProperty().bind(cover.heightProperty());

        return cover;
    }

    public VBox createVBoxForSliding(int padding, int gap,
                                      Node... nodes){
        VBox vbox = new VBox(gap);
        vbox.setPadding(new Insets(padding));

        vbox.getChildren().addAll(nodes);
        return vbox;
    }
}
