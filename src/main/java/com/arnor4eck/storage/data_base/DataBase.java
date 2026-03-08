package com.arnor4eck.storage.data_base;

import com.arnor4eck.util.exception.DataBaseNotInitializedException;
import com.zaxxer.hikari.HikariDataSource;
import kotlin.text.Charsets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase implements AutoCloseable {
    private final HikariDataSource dataSource;

    public DataBase(String jdbcUrl) throws DataBaseNotInitializedException {
        try {
            this.dataSource = initPool(jdbcUrl);
            initDb();
        } catch (Exception e) {
            throw new DataBaseNotInitializedException(e.getMessage(), e);
        }
    }

    private HikariDataSource initPool(String jdbcUrl){
        HikariDataSource source = new HikariDataSource();
        source.setJdbcUrl(jdbcUrl);
        source.setDriverClassName("org.sqlite.JDBC");
        source.setMaximumPoolSize(6);
        source.setConnectionInitSql("PRAGMA foreign_keys = ON"); // для всешних ключей
        source.setConnectionTimeout(30000);
        source.setIdleTimeout(600000);
        source.setMaxLifetime(1800000);

        return source;
    }

    private void initDb() throws SQLException, IOException {

        try(InputStream in = DataBase.class.getResourceAsStream("/init.sql");
            Statement preparedStatement = this.getConnection().createStatement()){

            String prepareDB = new String(in.readAllBytes(), Charsets.UTF_8);
            String[] scripts = prepareDB.split(";");

            for(String s: scripts)
                preparedStatement.execute(s);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
