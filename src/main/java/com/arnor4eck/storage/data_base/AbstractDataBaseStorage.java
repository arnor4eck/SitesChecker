package com.arnor4eck.storage.data_base;

import java.sql.Connection;

abstract class AbstractDataBaseStorage {

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

}
