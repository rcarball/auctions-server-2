package es.deusto.sd.auctions.external;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CurrencyServiceGatewayTest {

    @Test
    void skipsTheExternalCallWhenNoApiKeyIsConfigured() {
        CurrencyServiceGateway gateway = new CurrencyServiceGateway("https://example.invalid", "");

        assertTrue(gateway.getExchangeRate("EUR", "USD").isEmpty());
    }
}
