package es.deusto.sd.auctions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import es.deusto.sd.auctions.external.ICurrencyServiceGateway;

class CurrencyServiceTest {

    @Test
    void returnsEurWithoutCallingTheExternalGateway() {
        CurrencyService service = new CurrencyService((base, target) -> {
            throw new AssertionError("EUR must not use the external gateway");
        });

        assertEquals(1.0, service.getExchangeRate("eur").orElseThrow());
    }

    @Test
    void usesTheGatewayRateWhenAvailable() {
        ICurrencyServiceGateway gateway = (base, target) -> Optional.of(1.25);
        CurrencyService service = new CurrencyService(gateway);

        assertEquals(1.25, service.getExchangeRate("usd").orElseThrow());
    }

    @Test
    void usesTheLocalFallbackAndRejectsUnknownCurrencies() {
        CurrencyService service = new CurrencyService((base, target) -> Optional.empty());

        assertEquals(0.840, service.getExchangeRate("GBP").orElseThrow());
        assertTrue(service.getExchangeRate("JPY").isEmpty());
        assertTrue(service.getExchangeRate(null).isEmpty());
    }
}
