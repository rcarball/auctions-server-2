package es.deusto.sd.auctions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import es.deusto.sd.auctions.dao.ArticleRepository;
import es.deusto.sd.auctions.dao.CategoryRepository;
import es.deusto.sd.auctions.dao.UserRepository;
import es.deusto.sd.auctions.entity.Article;
import es.deusto.sd.auctions.entity.Category;
import es.deusto.sd.auctions.entity.User;

@DataJpaTest
class AuctionsServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private AuctionsService service;
    private User bidder;
    private Article article;

    @BeforeEach
    void setUp() {
        service = new AuctionsService(categoryRepository, articleRepository);
        User owner = userRepository.save(new User("owner", "owner@example.com", "owner-password-hash"));
        bidder = userRepository.save(new User("bidder", "bidder@example.com", "bidder-password-hash"));

        Category category = new Category("Electronics");
        article = new Article("Laptop", 100.0, new Date(System.currentTimeMillis() + 60_000), category, owner);
        category.addArticle(article);
        categoryRepository.saveAndFlush(category);
    }

    @Test
    void persistsAnIncreasingBidAndUpdatesTheWinner() {
        service.makeBid(bidder, article.getId(), 125.0);

        Article savedArticle = articleRepository.findById(article.getId()).orElseThrow();
        assertEquals(125.0, savedArticle.getCurrentPrice());
        assertEquals(bidder, savedArticle.getWinner());
        assertEquals(1, savedArticle.getBids().size());
    }

    @Test
    void rejectsBidsThatDoNotImproveTheCurrentPrice() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.makeBid(bidder, article.getId(), 100.0));

        assertEquals("Bid amount must be greater than the current price", exception.getMessage());
    }

    @Test
    void rejectsBidsForExpiredOrUnknownAuctions() {
        article.setAuctionEnd(new Date(System.currentTimeMillis() - 1));
        articleRepository.saveAndFlush(article);

        RuntimeException expired = assertThrows(RuntimeException.class,
                () -> service.makeBid(bidder, article.getId(), 125.0));
        RuntimeException unknown = assertThrows(RuntimeException.class,
                () -> service.makeBid(bidder, 999L, 125.0));

        assertEquals("Auction has ended", expired.getMessage());
        assertEquals("Article not found", unknown.getMessage());
    }

    @Test
    void rejectsNaNInfiniteAndNonPositiveBidAmounts() {
        assertInvalidAmount(Double.NaN);
        assertInvalidAmount(Double.POSITIVE_INFINITY);
        assertInvalidAmount(0.0);
        assertInvalidAmount(-1.0);
    }

    private void assertInvalidAmount(double amount) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.makeBid(bidder, article.getId(), amount));
        assertEquals("Bid amount must be a finite positive number", exception.getMessage());
    }
}
