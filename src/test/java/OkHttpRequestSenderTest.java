import com.arnor4eck.request_sender.OkHttpRequestSender;
import com.arnor4eck.util.exception.RequestNotSendException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OkHttpRequestSenderTest {

    private HttpClient mockHttpClient;
    private OkHttpRequestSender requestSender;

    @BeforeEach
    void setUp() throws Exception {
        mockHttpClient = Mockito.mock(HttpClient.class);
        requestSender = new OkHttpRequestSender();

        var field = OkHttpRequestSender.class.getDeclaredField("httpClient");
        field.setAccessible(true);
        field.set(requestSender, mockHttpClient);
    }

    @Test
    void testSendRequestShouldReturn200() throws Exception {
        String url = "https://example.com";
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("<html><body>Test</body></html>");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        com.arnor4eck.request_sender.HttpResponse response = requestSender.sendRequest(url);

        assertNotNull(response);
        assertEquals((short) 200, response.httpCode());
        assertNotNull(response.hash());
    }

    @Test
    void testSendRequestShouldReturn404() throws Exception {
        String url = "https://example.com/not_found";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(404);
        when(mockResponse.body()).thenReturn("Not Found");

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        com.arnor4eck.request_sender.HttpResponse response = requestSender.sendRequest(url);

        assertNotNull(response);
        assertEquals((short) 404, response.httpCode());
    }

    @Test
    void testSendRequestShouldThrowIOException() throws Exception {
        String url = "https://invalid-domain-that-does-not-exist.com";

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Соединение прервано"));

        RequestNotSendException e = Assertions.assertThrows(RequestNotSendException.class, () -> requestSender.sendRequest(url));

        assertEquals("Соединение прервано", e.getMessage());
        assertInstanceOf(IOException.class, e.getCause());
    }

    @Test
    void testSendRequestShouldReturn301() throws Exception {
        String url = "https://example.com/redirect";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);

        when(mockResponse.statusCode()).thenReturn(301);
        when(mockResponse.body()).thenReturn("Moved Permanently");

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        com.arnor4eck.request_sender.HttpResponse response = requestSender.sendRequest(url);

        assertNotNull(response);
        assertEquals((short) 301, response.httpCode());
    }
}
