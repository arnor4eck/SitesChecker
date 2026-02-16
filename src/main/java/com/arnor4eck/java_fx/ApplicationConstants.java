package com.arnor4eck.java_fx;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ApplicationConstants {
    public static final Color BASE_COLOR = Color.web("#1E1F22FF");
    public static final Image LOGO;

    static {
        LOGO = new Image(ApplicationConstants.class.getResourceAsStream("/images/logo.png"));
    }
}
