package com.arnor4eck.request_sender;

/** Рекорд-класс для записи ответа на запрос к сайту
 * @param hash Хеш-код HTML страницы
 * @param httpCode Код ответа сайта
 * */
public record HttpResponse(int httpCode, String hash) {}
