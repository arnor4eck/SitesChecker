package com.arnor4eck.util.exception;

import com.arnor4eck.request_sender.RequestSender;

/** Исключение, в случае, если RequestSender не смог отправить запрос
 * @see RequestSender
 * */
public class RequestNotSendException extends RuntimeException {
    public RequestNotSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
