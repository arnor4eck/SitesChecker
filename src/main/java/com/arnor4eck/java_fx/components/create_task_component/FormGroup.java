package com.arnor4eck.java_fx.components.create_task_component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.function.Predicate;

final class FormGroup extends VBox {

    private final Predicate<String> predicate;

    private final boolean shouldBeChecked;

    public FormGroup(Label label, Node field,
                     Predicate<String> predicate, boolean shouldBeChecked) {
        super();
        super.getChildren().addAll(label, field);
        this.predicate = predicate;
        this.shouldBeChecked = shouldBeChecked;
    }

    public Label getLabel(){
        return (Label) super.getChildren().getFirst();
    }

    public Node getField(){
        return super.getChildren().getLast();
    }

    private Object getValue(){
        Node field = getField();

        if(field instanceof TextField)
            return ((TextField) field).getText();
        if(field instanceof ComboBox)
            return ((ComboBox<?>) field).getValue();

        return null;
    }

    public void cleanField(){
        Node field = getField();

        if(field instanceof TextField)
            ((TextField) field).setText("");
        if(field instanceof ComboBox)
            ((ComboBox<?>) field).getSelectionModel().select(0);
    }

    @Override
    public ObservableList<Node> getChildren() {
        return FXCollections.unmodifiableObservableList(super.getChildren());
    }

    public String getText(){
        Object val = getValue();

        return val == null ? "" : val.toString();
    }

    public boolean validate(){
        if(!shouldBeChecked)
            return true;
        return predicate.test(getText());
    }
}
