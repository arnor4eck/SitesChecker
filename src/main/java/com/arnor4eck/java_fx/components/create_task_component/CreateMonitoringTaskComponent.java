package com.arnor4eck.java_fx.components.create_task_component;

import com.arnor4eck.java_fx.ApplicationUtils;
import com.arnor4eck.util.Logger;
import com.arnor4eck.util.request.CreateMonitoringTaskRequest;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.validator.routines.UrlValidator;

import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.function.Consumer;

@Getter
public final class CreateMonitoringTaskComponent {

    private final Pane createMonitoringTaskPane;

    @Getter(value = AccessLevel.NONE)
    private final UrlValidator urlValidator;

    @Getter(value = AccessLevel.NONE)
    private final FormGroupFactory formGroupFactory;

    public CreateMonitoringTaskComponent(Consumer<CreateMonitoringTaskRequest> createTask) {
        this.urlValidator = new UrlValidator(new String[]{"http", "https"},
                UrlValidator.ALLOW_2_SLASHES | UrlValidator.ALLOW_ALL_SCHEMES);
        this.formGroupFactory = new FormGroupFactory();
        this.createMonitoringTaskPane = setUpCreateMonitoringTaskPane(createTask);
    }

    private Pane setUpCreateMonitoringTaskPane(Consumer<CreateMonitoringTaskRequest> createTask){
        GridPane pane = new GridPane();
        pane.setBackground(ApplicationUtils.BASE_BACKGROUND);
        pane.setPadding(ApplicationUtils.BASE_INSETS);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);

        FormGroup name = formGroupFactory.createFormGroupTextField("Название",
                val -> !val.trim().isEmpty(), true);

        FormGroup url = formGroupFactory.createFormGroupTextField("Ссылка",
                urlValidator::isValid,
                true);

        FormGroup period = formGroupFactory.createFormGroupTextField("Период",
                val -> {
                    try {
                        return Integer.parseInt(val) > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }, true);

        ComboBox<String> units = new ComboBox<>(FXCollections.observableArrayList(
                "Секунда",
                "Минута",
                "Час"));
        units.setMinWidth(150);
        units.getSelectionModel().select(0);

        FormGroup unitsPick = formGroupFactory.createFormGroup("Единица времени", units,
                null, false);


        Button create = new Button("Создать");

        create.setOnAction(event -> {
            if(this.validate()){
                createTask.accept(new CreateMonitoringTaskRequest(
                        name.getText(),
                        url.getText(),
                        Long.parseLong(period.getText()),
                        parseStringToChronoUnit(unitsPick.getText())
                ));

                Logger.getInstance().info("Сайт '%s' был добавлен в список мониторинга"
                        .formatted(name.getText()));

                ApplicationUtils.alert(Alert.AlertType.INFORMATION,
                        "Успешно!",
                        "Сайт '%s' был успешно добавлен в список мониторинга".formatted(name.getText()))
                        .show();

                cleanFields();
            }
        });

        Button clean = new Button("Очистить");
        clean.setOnAction(ev -> cleanFields());

        HBox vbox = new HBox(10, create, clean);

        pane.add(name, 0, 0);
        pane.add(url, 0, 1);
        pane.add(period, 1, 0);
        pane.add(unitsPick, 1, 1);
        pane.add(vbox, 0, 2, 2, 1);

        return pane;
    }

    private boolean validate(){

        Iterator<Node> it = this.createMonitoringTaskPane.getChildren()
                .stream()
                .filter(n -> n instanceof FormGroup)
                .iterator();

        while (it.hasNext()) {
            FormGroup fr = (FormGroup) it.next();
            if(!fr.validate()){
                ApplicationUtils.alert(Alert.AlertType.ERROR,
                        "Ошибка валидации!",
                        "Ошибка в поле: " + fr.getLabel().getText())
                        .show();

                return false;
            }
        }

        return true;
    }

    private void cleanFields(){
        this.createMonitoringTaskPane
                .getChildren()
                .stream()
                .filter(n -> n instanceof FormGroup)
                .forEach(fg -> ((FormGroup) fg).cleanField());
    }

    private ChronoUnit parseStringToChronoUnit(String value){
        return switch (value){
            case "Секунда" -> ChronoUnit.SECONDS;
            case "Минута" -> ChronoUnit.MINUTES;
            case "Час" -> ChronoUnit.HOURS;
            default -> throw new IllegalArgumentException();
        };
    }
}
