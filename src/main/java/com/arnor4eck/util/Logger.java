package com.arnor4eck.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/** Класс логгера
 * */
public class Logger {

    private final Consumer<String> logger;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static class Holder{
        private static final Logger logger = new Logger(System.out::println);
    }

    private enum Type{
        ERROR,
        WARNING,
        INFO
    }

    private Logger(Consumer<String> log){
        this.logger = log;
    };

    public static Logger getInstance(){
        return Holder.logger;
    }

    private synchronized void log(Type type,
                     String message){
        logger.accept(String.format(
                "[%s] %s // %s",
                type.toString(), LocalDateTime.now().format(formatter), message
        ));
    }

    public void info(String message){
        log(Type.INFO, message);
    }

    public void warn(String message){
        log(Type.WARNING, message);
    }

    public void error(String message){
        log(Type.ERROR, message);
    }
}
