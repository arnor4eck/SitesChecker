package com.arnor4eck.java_fx.utils;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.LinkedList;
import java.util.List;

public final class TableViewBuilder<V> {

    private final List<TableColumn<V, ?>> columns = new LinkedList<>();

    private TableViewBuilder(){}

    public static <V> TableViewBuilder<V> builder(){
        return new TableViewBuilder<>();
    }

    public <T> TableViewBuilder<V> addColumn(String label,
                                                 Callback<TableColumn.CellDataFeatures<V, T>, ObservableValue<T>> getter){
        TableColumn<V, T> column = new TableColumn<>(label);
        column.setCellValueFactory(getter);
        columns.add(column);

        return this;
    }

    public TableView<V> build(){
        TableView<V> tableView = new TableView<>();
        tableView.getColumns().addAll(columns);

        return tableView;
    }
}
