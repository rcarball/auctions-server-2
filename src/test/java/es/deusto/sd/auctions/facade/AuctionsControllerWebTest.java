/**
 * Entirely generated with ChatGPT-6.1 in September and October 2026.
 * Reviewed and updated with assistance from ChatGPT-6.1 Sol (October 2026).
 */
package es.deusto.sd.auctions.facade;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.deusto.sd.auctions.entity.Category;
import es.deusto.sd.auctions.entity.User;
import es.deusto.sd.auctions.service.AuctionException;
import es.deusto.sd.auctions.service.AuctionException.Reason;
import es.deusto.sd.auctions.service.AuctionsService;
import es.deusto.sd.auctions.service.AuthService;
import es.deusto.sd.auctions.service.CurrencyService;

@ExtendWith(MockitoExtension.class)
class AuctionsControllerWebTest {

    @Mock private AuctionsService auctionsService;
    @Mock private AuthService authService;
    @Mock private CurrencyService currencyService;

    private MockMvc mockMvc;
    private final User user = new User("student", "student@example.com", "password");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new AuctionsController(auctionsService, authService, currencyService)).build();
    }

    @Test
    void categoriesEndpointReturnsDtos() throws Exception {
        when(auctionsService.getCategories()).thenReturn(List.of(new Category("Electronics")));

        mockMvc.perform(get("/auctions/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void articlesEndpointReturnsNotFoundForUnknownCategory() throws Exception {
        when(auctionsService.getArticlesByCategoryName("Unknown"))
                .thenThrow(new AuctionException(Reason.NOT_FOUND, "No matching category"));

        mockMvc.perform(get("/auctions/categories/Unknown/articles").param("currency", "EUR"))
                .andExpect(status().isNotFound());
    }

    @Test
    void preservesLiteralPlusAndPercentCharactersInCategoryNames() throws Exception {
        when(auctionsService.getArticlesByCategoryName("Books+100%" )).thenReturn(List.of());
        when(currencyService.getExchangeRate("EUR")).thenReturn(Optional.of(1.0));
        mockMvc.perform(get("/auctions/categories/{category}/articles", "Books+100%")
                .param("currency", "EUR")).andExpect(status().isNoContent());
        verify(auctionsService).getArticlesByCategoryName("Books+100%");
    }

    @Test
    void rejectsUnsupportedCurrencyForAnEmptyCategory() throws Exception {
        when(auctionsService.getArticlesByCategoryName("Empty")).thenReturn(List.of());
        when(currencyService.getExchangeRate("JPY")).thenReturn(Optional.empty());

        mockMvc.perform(get("/auctions/categories/Empty/articles").param("currency", "JPY"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bidEndpointRejectsUnauthenticatedUsers() throws Exception {
        mockMvc.perform(post("/auctions/articles/1/bid")
                .param("amount", "125")
                .param("currency", "EUR")
                .contentType(MediaType.TEXT_PLAIN)
                .content("invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void bidEndpointReturnsBadRequestForInvalidAmount() throws Exception {
        when(authService.getUserByToken("token-123")).thenReturn(user);
        when(currencyService.getExchangeRate("EUR")).thenReturn(Optional.of(1.0));
        doThrow(new AuctionException(Reason.INVALID_AMOUNT, "Invalid amount"))
                .when(auctionsService).makeBid(user, 1L, 0.0, 1.0);

        mockMvc.perform(post("/auctions/articles/1/bid")
                .param("amount", "0")
                .param("currency", "EUR")
                .contentType(MediaType.TEXT_PLAIN)
                .content("token-123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bidEndpointReturnsConflictForLowBid() throws Exception {
        when(authService.getUserByToken("token-123")).thenReturn(user);
        when(currencyService.getExchangeRate("EUR")).thenReturn(Optional.of(1.0));
        doThrow(new AuctionException(Reason.BID_TOO_LOW, "Bid too low"))
                .when(auctionsService).makeBid(user, 1L, 100.0, 1.0);

        mockMvc.perform(post("/auctions/articles/1/bid")
                .param("amount", "100")
                .param("currency", "EUR")
                .contentType(MediaType.TEXT_PLAIN)
                .content("token-123"))
                .andExpect(status().isConflict());
    }

    @Test
    void bidEndpointPassesTheAmountAndRateToTheService() throws Exception {
        when(authService.getUserByToken("token-123")).thenReturn(user);
        when(currencyService.getExchangeRate("GBP")).thenReturn(Optional.of(0.8));

        mockMvc.perform(post("/auctions/articles/1/bid")
                .param("amount", "100")
                .param("currency", "GBP")
                .contentType(MediaType.TEXT_PLAIN)
                .content("token-123"))
                .andExpect(status().isNoContent());

        verify(auctionsService).makeBid(user, 1L, 100.0, 0.8);
    }
}
