package es.deusto.sd.auctions.external;

import java.util.Optional;

public interface ICurrencyServiceGateway {
	public Optional<Double> getExchangeRate(String baseCurrency, String targetCurrency);
}
