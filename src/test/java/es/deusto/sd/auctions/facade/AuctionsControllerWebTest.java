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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.deusto.sd.auctions.entity.Category;
import es.deusto.sd.auctions.entity.User;
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
                .thenThrow(new RuntimeException("Category not found"));

        mockMvc.perform(get("/auctions/categories/Unknown/articles").param("currency", "EUR"))
                .andExpect(status().isNotFound());
    }

    @Test
    void bidEndpointRejectsUnauthenticatedUsers() throws Exception {
        mockMvc.perform(post("/auctions/articles/1/bid")
                .param("amount", "125")
                .param("currency", "EUR")
                .content("invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void bidEndpointReturnsBadRequestForInvalidAmount() throws Exception {
        when(authService.getUserByToken("token-123")).thenReturn(user);
        when(currencyService.getExchangeRate("EUR")).thenReturn(Optional.of(1.0));
        doThrow(new IllegalArgumentException("Bid amount must be a finite positive number"))
                .when(auctionsService).makeBid(user, 1L, 0.0);

        mockMvc.perform(post("/auctions/articles/1/bid")
                .param("amount", "0")
                .param("currency", "EUR")
                .content("token-123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bidEndpointReturnsConflictForLowBid() throws Exception {
        when(authService.getUserByToken("token-123")).thenReturn(user);
        when(currencyService.getExchangeRate("EUR")).thenReturn(Optional.of(1.0));
        doThrow(new RuntimeException("Bid amount must be greater than the current price"))
                .when(auctionsService).makeBid(user, 1L, 100.0);

        mockMvc.perform(post("/auctions/articles/1/bid")
                .param("amount", "100")
                .param("currency", "EUR")
                .content("token-123"))
                .andExpect(status().isConflict());
    }

    @Test
    void bidEndpointConvertsCurrencyAndReturnsNoContent() throws Exception {
        when(authService.getUserByToken("token-123")).thenReturn(user);
        when(currencyService.getExchangeRate("GBP")).thenReturn(Optional.of(0.8));

        mockMvc.perform(post("/auctions/articles/1/bid")
                .param("amount", "100")
                .param("currency", "GBP")
                .content("token-123"))
                .andExpect(status().isNoContent());

        verify(auctionsService).makeBid(user, 1L, 125.0);
    }
}
