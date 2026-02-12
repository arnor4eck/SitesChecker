package com.arnor4eck.request_sender;

import com.arnor4eck.util.exception.RequestNotSendException;

/** Модуль, отвечающий за отправку запросов к сайту
 * */
public interface RequestSender {
    /** Метод, отправляющий запрос
     * @param url Ссылка сайта
     * @return Объект ответа на запрос
     * @throws RequestNotSendException
     * */
    HttpResponse sendRequest(String url) throws RequestNotSendException;
}
