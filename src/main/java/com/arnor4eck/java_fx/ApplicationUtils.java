package com.arnor4eck.java_fx;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ApplicationUtils {
    public static final Color PRIMARY_COLOR = Color.web("#26282EFF");
    public static final Color LOG_COLOR = Color.web("#3F3F3FFF");
    public static final Background BASE_BACKGROUND = new Background(new BackgroundFill(Color.web("#1E1F22FF"), CornerRadii.EMPTY, Insets.EMPTY));
    public static final Insets BASE_INSETS = new Insets(10);
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final Image LOGO;
    static {
        LOGO = new Image(ApplicationUtils.class.getResourceAsStream("/images/icon-logo.png"));
    }

    public static Alert alert(Alert.AlertType type,
                           String headerText,
                           String contentText){
        Alert alert = new Alert(type);

        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setTitle(headerText);

        return alert;
    }

    public static String parseChronoUnitToString(ChronoUnit value) {
        return switch (value) {
            case SECONDS -> "Секунда";
            case MINUTES -> "Минута";
            case HOURS -> "Час";
            default -> throw new IllegalArgumentException("Unsupported unit: " + value);
        };
    }

    public ChronoUnit parseStringToChronoUnit(String value){
        return switch (value){
            case "Секунда" -> ChronoUnit.SECONDS;
            case "Минута" -> ChronoUnit.MINUTES;
            case "Час" -> ChronoUnit.HOURS;
            default -> throw new IllegalArgumentException();
        };
    }

    private ApplicationUtils() {}
}
