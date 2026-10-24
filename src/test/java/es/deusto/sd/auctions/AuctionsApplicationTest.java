/**
 * Entirely generated with ChatGPT-6.1 in September and October 2026.
 * Reviewed and updated with assistance from ChatGPT-6.1 Sol (October 2026).
 */
package es.deusto.sd.auctions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.boot.test.context.SpringBootTest;

import tools.jackson.databind.json.JsonMapper;
import es.deusto.sd.auctions.dao.ArticleRepository;
import es.deusto.sd.auctions.dao.CategoryRepository;
import es.deusto.sd.auctions.dao.UserRepository;
import es.deusto.sd.auctions.entity.User;
import es.deusto.sd.auctions.service.AuctionsService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:auctions-http-tests;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "currency.api.key="
})
class AuctionsApplicationTest {

    @Value("${local.server.port}")
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();

    @Autowired private DataInitializer initializer;
    @Autowired private UserRepository users;
    @Autowired private ArticleRepository articles;
    @Autowired private CategoryRepository categories;
    @Autowired private AuctionsService auctionsService;
    @Autowired private PlatformTransactionManager transactionManager;

    @Test
    void acceptsOnlyOneOfTwoConcurrentEqualBids() throws Exception {
        User bidder = users.findByEmail("batman@dc.com").orElseThrow();
        var article = articles.saveAndFlush(new es.deusto.sd.auctions.entity.Article(
                "Concurrent bidding test", 100.0, new java.util.Date(System.currentTimeMillis() + 60_000),
                categories.findByName("Electronics").orElseThrow(), bidder));
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            java.util.concurrent.Callable<Boolean> bid = () -> {
                ready.countDown();
                if (!start.await(5, TimeUnit.SECONDS)) throw new AssertionError("Concurrent start timed out");
                try {
                    auctionsService.makeBid(bidder, article.getId(), 125.0);
                    return true;
                } catch (RuntimeException exception) {
                    assertEquals("Bid amount must be greater than the current price", exception.getMessage());
                    return false;
                }
            };
            var first = executor.submit(bid);
            var second = executor.submit(bid);
            assertTrue(ready.await(5, TimeUnit.SECONDS));
            start.countDown();
            assertEquals(1, (first.get(10, TimeUnit.SECONDS) ? 1 : 0) + (second.get(10, TimeUnit.SECONDS) ? 1 : 0));
            var saved = articles.findById(article.getId()).orElseThrow();
            assertEquals(125.0, saved.getCurrentPrice());
            assertEquals(1, saved.getBids().size());
            assertEquals(bidder, saved.getWinner());
        } finally {
            start.countDown();
            articles.deleteById(article.getId());
        }
    }

    @Test
    void doesNotDuplicateSeedDataOnASecondInitialization() throws Exception {
        long articleCount = articles.count();
        long userCount = users.count();
        long categoryCount = categories.count();
        initializer.initData(categories, users, articles).run();
        assertEquals(articleCount, articles.count());
        assertEquals(userCount, users.count());
        assertEquals(categoryCount, categories.count());
    }

    @Test
    void rollsBackSeedDataIfInitializationFails() throws Exception {
        CategoryRepository categories = mock(CategoryRepository.class);
        UserRepository seedUsers = mock(UserRepository.class);
        when(categories.count()).thenReturn(0L);
        doAnswer(invocation -> {
            users.saveAndFlush(new User("rollback-test", "rollback@example.com", "hash"));
            return java.util.List.of();
        }).when(seedUsers).saveAll(any());
        doThrow(new IllegalStateException("Simulated initialization failure")).when(categories).saveAll(any());
        try {
            // Calling a @Bean method on the configuration proxy returns its existing runner.
            // Use a fresh factory here so that the failure is injected into the runner being tested.
            var runner = new DataInitializer(transactionManager).initData(categories, seedUsers, articles);
            assertThrows(IllegalStateException.class, runner::run);
            assertTrue(users.findByEmail("rollback@example.com").isEmpty());
        } finally {
            users.findByEmail("rollback@example.com").ifPresent(users::delete);
        }
    }

    @Test
    void servesOpenApiAndSwaggerUi() throws Exception {
        HttpResponse<String> api = get("/v3/api-docs");
        assertEquals(200, api.statusCode());
        assertTrue(api.body().contains("/auth/login"));
        assertTrue(api.body().contains("/auctions/articles/{articleId}/bid"));
        HttpResponse<String> ui = get("/swagger-ui/index.html");
        assertEquals(200, ui.statusCode());
        assertTrue(ui.body().contains("swagger-ui"));
    }

    @Test
    void completesTheClassroomLoginBidAndLogoutWorkflow() throws Exception {
        assertEquals(200, get("/auctions/categories").statusCode());
        assertEquals(200, get("/auctions/categories/Sporting%20Goods/articles?currency=EUR").statusCode());
        assertEquals(200, get("/auctions/articles/1/details?currency=EUR").statusCode());
        String credentials = "{\"email\":\"batman@dc.com\",\"password\":\"Batm@n123!\"}";
        HttpResponse<String> login = post("/auth/login", "application/json", credentials);
        assertEquals(200, login.statusCode());
        String token = login.body();
        assertEquals(204, post("/auctions/articles/1/bid?amount=1500&currency=EUR", "text/plain", token).statusCode());
        var details = JsonMapper.builder().build().readTree(get("/auctions/articles/1/details?currency=EUR").body());
        assertEquals(1500.0, details.get("currentPrice").asDouble());
        assertEquals(1, details.get("bids").asInt());
        assertEquals(409, post("/auctions/articles/1/bid?amount=1500&currency=EUR", "text/plain", token).statusCode());
        assertEquals(204, post("/auth/logout", "text/plain", token).statusCode());
        assertEquals(401, post("/auctions/articles/1/bid?amount=1600&currency=EUR", "text/plain", token).statusCode());
    }

    @Test
    void rejectsIncompleteCredentialsWithoutAnInternalServerError() throws Exception {
        assertEquals(401, post("/auth/login", "application/json", "{}").statusCode());
        assertEquals(401, post("/auth/login", "application/json", "{\"email\":\"batman@dc.com\"}").statusCode());
    }

    @Test
    void validatesCurrencyArticleIdsAndBidAmounts() throws Exception {
        assertEquals(400, get("/auctions/articles/1/details?currency=INVALID").statusCode());
        assertEquals(404, get("/auctions/articles/999/details?currency=EUR").statusCode());
        String credentials = "{\"email\":\"batman@dc.com\",\"password\":\"Batm@n123!\"}";
        HttpResponse<String> login = post("/auth/login", "application/json", credentials);
        assertEquals(200, login.statusCode());
        String token = login.body();
        try {
            assertEquals(400, post("/auctions/articles/2/bid?amount=NaN&currency=EUR", "text/plain", token).statusCode());
            assertEquals(400, post("/auctions/articles/2/bid?amount=0&currency=EUR", "text/plain", token).statusCode());
            assertEquals(404, post("/auctions/articles/999/bid?amount=2000&currency=EUR", "text/plain", token).statusCode());
        } finally {
            assertEquals(204, post("/auth/logout", "text/plain", token).statusCode());
        }
    }

    private HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String contentType, String body) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .header("Content-Type", contentType).POST(HttpRequest.BodyPublishers.ofString(body)).build(),
                HttpResponse.BodyHandlers.ofString());
    }
}
