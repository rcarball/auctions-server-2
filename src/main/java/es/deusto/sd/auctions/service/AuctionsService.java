/**
 * Initially generated with ChatGPT 4o and adapted using GitHub Copilot.
 * Reviewed and updated with assistance from Claude Opus 4.8 (July 2026)
 * and ChatGPT-6.1 Sol (October 2026).
 */
package es.deusto.sd.auctions.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.deusto.sd.auctions.dao.ArticleRepository;
import es.deusto.sd.auctions.dao.CategoryRepository;
import es.deusto.sd.auctions.entity.Article;
import es.deusto.sd.auctions.entity.Bid;
import es.deusto.sd.auctions.entity.Category;
import es.deusto.sd.auctions.entity.User;
import es.deusto.sd.auctions.service.AuctionException.Reason;

@Service
public class AuctionsService {

	private final CategoryRepository categoryRepository;
	private final ArticleRepository articleRepository;    

    public AuctionsService(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
    	this.categoryRepository = categoryRepository;
    	this.articleRepository = articleRepository;
    }	    
	    
	// Get all categories
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}

	// Get articles of a specific category
	public List<Article> getArticlesByCategoryName(String categoryName) {
	    Optional<Category> category = categoryRepository.findByName(categoryName);

        if (category.isEmpty()) {
            throw new AuctionException(Reason.NOT_FOUND, "Category not found");
        }
        
        return category.get().getArticles();
    }
	
	// Get article by id
	public Article getArticleById(long articleId) {
		Optional<Article> article = articleRepository.findById(articleId);
		
		return article.isPresent() ? article.get() : null;
	}

	// Make a bid on an article.
	// @Transactional makes the whole "read - check - register bid - save" a single unit
	// of work, and the pessimistic write lock (findByIdForUpdate) serializes concurrent
	// bids on the same article, preventing lost updates.
	@Transactional
	public void makeBid(User user, long articleId, double amount) {
		makeBid(user, articleId, amount, 1.0);
	}

	@Transactional
	public void makeBid(User user, long articleId, double amount, double exchangeRate) {
		double amountInEur = amount / exchangeRate;
		// A price must be a meaningful, finite number. In particular, comparisons with
		// NaN are always false, so accepting it would bypass the current-price check.
		if (!Double.isFinite(amountInEur) || amountInEur <= 0.0) {
			throw new AuctionException(Reason.INVALID_AMOUNT, "Bid amount must be a finite positive number");
		}

		// Retrieve the article by ID, locking its row for the duration of the transaction
		Optional<Article> article = articleRepository.findByIdForUpdate(articleId);

		if (article.isEmpty()) {
			throw new AuctionException(Reason.NOT_FOUND, "Article not found");
		}

		Article foundArticle = article.get();

		// A bid can only be placed while the auction is still open.
		if (foundArticle.getAuctionEnd() != null
				&& System.currentTimeMillis() >= foundArticle.getAuctionEnd().getTime()) {
			throw new AuctionException(Reason.AUCTION_ENDED, "Auction has ended");
		}

		double currentPrice = foundArticle.getCurrentPrice();
		// Compare against the published price before converting back to EUR.
		// Also require a strict increase in the stored amount after conversion.
		if (amount <= currentPrice * exchangeRate || amountInEur <= currentPrice) {
			throw new AuctionException(Reason.BID_TOO_LOW, "Bid amount must be greater than the current price");
		}

		// Create a new bid and associate it with the article
		Bid bid = new Bid(System.currentTimeMillis(), amountInEur, foundArticle, user);
		foundArticle.getBids().add(bid);

        // Save updated article with the new bid
        articleRepository.save(foundArticle);
	}
}
