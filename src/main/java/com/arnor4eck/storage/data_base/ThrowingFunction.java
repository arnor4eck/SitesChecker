package com.arnor4eck.storage.data_base;

import java.sql.SQLException;

@FunctionalInterface
interface ThrowingFunction<T,R>{
    R apply(T t) throws SQLException;
}
