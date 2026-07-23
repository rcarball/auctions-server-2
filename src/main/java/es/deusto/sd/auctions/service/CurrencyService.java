package es.deusto.sd.auctions.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.auctions.external.ICurrencyServiceGateway;

@Service
public class CurrencyService {

	// Locally cached rates (relative to EUR) used only as a fallback when the
	// external currency service is unavailable.
	public enum ExchangeRate {
		USD(1.104),
		GBP(0.840);

		private double rate;

		ExchangeRate(double rate) {
			this.rate = rate;
		}
	}

	private static final String DEFAULT_CURRENCY = "EUR";
	private final ICurrencyServiceGateway currencyGateway;

    public CurrencyService(ICurrencyServiceGateway currencyGateway) {
        this.currencyGateway = currencyGateway;
    }

    public Optional<Double> getExchangeRate(String currency) {
    	if (currency == null) {
    		return Optional.empty();
    	}

    	String normalizedCurrency = currency.toUpperCase();

    	// The base currency is always 1:1 with itself and never needs the external
    	// service. This guarantees that EUR (the currency all amounts are stored in)
    	// keeps working even if the external API is down.
    	if (DEFAULT_CURRENCY.equals(normalizedCurrency)) {
    		return Optional.of(1.0);
    	}

    	// Primary source: external currency gateway.
    	Optional<Double> exchangeRate = currencyGateway.getExchangeRate(DEFAULT_CURRENCY, normalizedCurrency);

    	if (exchangeRate.isPresent()) {
    		return exchangeRate;
    	}

    	// Fallback: locally cached rates if the external service is unavailable.
    	// An unsupported currency results in an empty Optional (translated to 400).
    	try {
    		return Optional.of(ExchangeRate.valueOf(normalizedCurrency).rate);
    	} catch (IllegalArgumentException ex) {
    		return Optional.empty();
    	}
    }
}
