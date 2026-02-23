package com.arnor4eck.java_fx;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ApplicationConstants {
    public static final Color BASE_COLOR = Color.web("#1E1F22FF");
    public static final Color PRIMARY_COLOR = Color.web("#26282EFF");
    public static final Color LOG_COLOR = Color.web("#3F3F3FFF");
    public static final Background BASE_BACKGROUND = new Background(new BackgroundFill(ApplicationConstants.BASE_COLOR, CornerRadii.EMPTY, Insets.EMPTY));

    public static final Image LOGO;
    static {
        LOGO = new Image(ApplicationConstants.class.getResourceAsStream("/images/icon-logo.png"));
    }
}
