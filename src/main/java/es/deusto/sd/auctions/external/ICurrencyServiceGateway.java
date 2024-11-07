package es.deusto.sd.auctions.external;

import java.util.Optional;

public interface ICurrencyServiceGateway {
	public Optional<Float> getExchangeRate(String baseCurrency, String targetCurrency);
}
