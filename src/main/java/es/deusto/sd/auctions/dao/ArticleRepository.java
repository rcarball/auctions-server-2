/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import es.deusto.sd.auctions.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}