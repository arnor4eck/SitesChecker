package com.arnor4eck.request_sender;

import com.arnor4eck.util.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/** Реализация RequestSender через библиотеку OkHttp
 * */
public class OkHttpRequestSender implements RequestSender {

    /** Клиент OkHttp
     * */
    private final HttpClient httpClient;

    /** @see Logger
     * */
    private final Logger logger = Logger.getInstance();

    public OkHttpRequestSender() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))
                .build();
    }

    @Override
    public com.arnor4eck.request_sender.HttpResponse sendRequest(String url)
            throws RequestNotSendException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.of(5, ChronoUnit.SECONDS))
                .GET()
                .header("User-Agent", "SitesChecker/1.0")
                .build();

        try {
            HttpResponse<String> r = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return new com.arnor4eck.request_sender.HttpResponse(r.statusCode(),
                    String.valueOf(r.body().hashCode()));

        } catch (IOException | InterruptedException e) {
            logger.warn(
                    String.format("Ошибка отправки запроса в сайту '%s': %s",
                            url, e.getMessage()));

            throw new RequestNotSendException(e.getMessage(), e);
        }
    }
}
