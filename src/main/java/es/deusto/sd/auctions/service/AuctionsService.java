/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.auctions.dao.ArticleRepository;
import es.deusto.sd.auctions.dao.CategoryRepository;
import es.deusto.sd.auctions.entity.Article;
import es.deusto.sd.auctions.entity.Bid;
import es.deusto.sd.auctions.entity.Category;
import es.deusto.sd.auctions.entity.User;

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
            throw new RuntimeException("Category not found");
        }
        
        return category.get().getArticles();
    }
	
	// Get article by id
	public Article getArticleById(long articleId) {
		Optional<Article> article = articleRepository.findById(articleId);
		
		return article.isPresent() ? article.get() : null;
	}

	// Make a bid on an article
	public void makeBid(User user, long articleId, float amount) {
		// Retrieve the article by ID
		Optional<Article> article = articleRepository.findById(articleId);

		if (article.isEmpty()) {
			throw new RuntimeException("Article not found");
		}

		if (amount <= article.get().getCurrentPrice()) {
			throw new RuntimeException("Bid amount must be greater than the current price");
		}
		
		// Create a new bid and associate it with the article
		Bid bid = new Bid(System.currentTimeMillis(), amount, article.get(), user);
		article.get().getBids().add(bid);
		
        // Save updated article with the new bid
        articleRepository.save(article.get());
	}
}