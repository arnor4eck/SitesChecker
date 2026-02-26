package com.arnor4eck.java_fx.components.create_task_component;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

final class FormGroupFactory {

    public FormGroup createFormGroupTextField(String labelName,
                                              Predicate<String> predicate,
                                              boolean shouldBeChecked){
        return createFormGroup(labelName, new TextField(), predicate, shouldBeChecked);
    }

    public FormGroup createFormGroup(String labelName,
                                      Node field,
                                      Predicate<String> predicate,
                                      boolean shouldBeChecked) {
        Label label = new Label(labelName);
        label.setStyle("-fx-font-size: 14; -fx-text-fill: white;");

        return new FormGroup(label, field,
                predicate, shouldBeChecked);
    }
}
