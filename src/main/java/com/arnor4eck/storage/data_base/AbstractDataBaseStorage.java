package com.arnor4eck.storage.data_base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class AbstractDataBaseStorage<T> {

    private final DataBase db;

    public AbstractDataBaseStorage(DataBase db) {
        this.db = db;
    }

    protected final Connection getConnection() {
        try {
            return db.getConnection();
        }catch(Exception e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

    protected final <R> R executeQuery(String query,
                               ThrowingFunction<PreparedStatement, R> function) throws SQLException {
        try(Connection con = this.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
            return function.apply(ps);
        }
    }

    protected abstract T extract(ResultSet rs) throws SQLException;
}
