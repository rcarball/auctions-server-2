package es.deusto.sd.auctions.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.auctions.external.CurrencyServiceGateway;

@Service
public class CurrencyService {

	public enum ExchangeRate {
		USD(1.104f),
		GBP(0.840f);
			
		private float rate;

		ExchangeRate(float rate) {
			this.rate = rate;
		}
	}
	
	private static final String DEFAULT_CURRENCY = "EUR";
	private final CurrencyServiceGateway currencyGateway;

    public CurrencyService(CurrencyServiceGateway currencyGateway) {
        this.currencyGateway = currencyGateway;
    }	
	
    public Optional<Float> getExchangeRate(String currency) {
    	Optional<Float> exchangeRate = currencyGateway.getExchangeRate(DEFAULT_CURRENCY, currency.toUpperCase());

    	try {
			if (exchangeRate.isPresent()) {
				return exchangeRate;
			} else {
				return Optional.of(ExchangeRate.valueOf(currency).rate);
			}
    	} catch(Exception ex) {
    		return Optional.empty();
    	}
    }
}