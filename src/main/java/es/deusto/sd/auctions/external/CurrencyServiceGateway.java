/**
 * This code was originally generated with ChatGPT 4o and adapted using GitHub
 * Copilot. It was reviewed, corrected and updated in July 2026 with the
 * assistance of Claude Opus 4.8 (Anthropic).
 */
package es.deusto.sd.auctions.external;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class CurrencyServiceGateway implements ICurrencyServiceGateway {

    // Injected from configuration (application.properties / environment variables)
    // so that no secret is hard-coded in the source code.
    private final String apiUrl;
    private final String apiKey;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CurrencyServiceGateway(
            @Value("${currency.api.url}") String apiUrl,
            @Value("${currency.api.key}") String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        // A connect timeout prevents the request thread from blocking forever if the
        // external service is unreachable.
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @SuppressWarnings("unchecked")
    @Override
	public Optional<Double> getExchangeRate(String baseCurrency, String targetCurrency) {
        // Build the URL
        String url = apiUrl + "?apikey=" + apiKey + "&base_currency=" + baseCurrency + "&currencies=" + targetCurrency;

        try {
            // Create the request (with a read timeout as well)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            // Send the request and obtain the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        	// If response is OK, parse the response body
        	if (response.statusCode() == 200) {
				Map<String, Object> exchangeRates = objectMapper.readValue(response.body(), Map.class);
				Object data = exchangeRates.get("data");

				if (!(data instanceof Map)) {
					return Optional.empty();
				}

				Object rate = ((Map<String, Object>) data).get(targetCurrency);

				// The requested currency may not be present in the response.
				if (rate == null) {
					return Optional.empty();
				}

				return Optional.of(Double.parseDouble(rate.toString()));
			} else {
				return Optional.empty();
			}
        } catch (Exception ex) {
        	return Optional.empty();
        }
    }
}
