/**
 * Entirely generated with ChatGPT-6.1 in September and October 2026.
 * Reviewed and updated with assistance from ChatGPT-6.1 Sol (October 2026).
 */
package es.deusto.sd.auctions.external;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import com.sun.net.httpserver.HttpServer;

import org.junit.jupiter.api.Test;

class CurrencyServiceGatewayTest {

    @Test
    void skipsTheExternalCallWhenNoApiKeyIsConfigured() {
        CurrencyServiceGateway gateway = new CurrencyServiceGateway("https://example.invalid", "");

        assertTrue(gateway.getExchangeRate("EUR", "USD").isEmpty());
    }

    @Test
    void parsesValidRatesAndRejectsInvalidResponses() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        AtomicReference<String> body = new AtomicReference<>();
        AtomicReference<Integer> status = new AtomicReference<>(200);
        server.createContext("/latest", exchange -> {
            byte[] bytes = body.get().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status.get(), bytes.length);
            try (var output = exchange.getResponseBody()) {
                output.write(bytes);
            }
        });
        server.start();
        try {
            CurrencyServiceGateway gateway = new CurrencyServiceGateway(
                    "http://127.0.0.1:" + server.getAddress().getPort() + "/latest", "test-key");
            body.set("{\"data\":{\"USD\":1.25}}");
            assertEquals(1.25, gateway.getExchangeRate("EUR", "USD").orElseThrow());
            for (String response : new String[] {"{}", "{\"data\":{}}", "invalid-json",
                    "{\"data\":{\"USD\":0}}", "{\"data\":{\"USD\":-1}}",
                    "{\"data\":{\"USD\":\"NaN\"}}", "{\"data\":{\"USD\":\"Infinity\"}}"}) {
                body.set(response);
                assertTrue(gateway.getExchangeRate("EUR", "USD").isEmpty(), response);
            }
            status.set(503);
            body.set("{\"data\":{\"USD\":1.25}}");
            assertTrue(gateway.getExchangeRate("EUR", "USD").isEmpty());
        } finally {
            server.stop(0);
        }
    }
}
