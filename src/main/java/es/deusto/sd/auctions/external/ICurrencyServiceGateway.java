/**
 * Initially generated with ChatGPT 4o and adapted using GitHub Copilot.
 * Reviewed and updated with assistance from Claude Opus 4.8 (July 2026)
 * and ChatGPT-6.1 Sol (October 2026).
 */
package es.deusto.sd.auctions.external;

import java.util.Optional;

public interface ICurrencyServiceGateway {
	public Optional<Double> getExchangeRate(String baseCurrency, String targetCurrency);
}
