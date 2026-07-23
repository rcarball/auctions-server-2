/**
 * This code was originally generated with ChatGPT 4o and adapted using GitHub
 * Copilot. It was reviewed, corrected and updated in July 2026 with the
 * assistance of Claude Opus 4.8 (Anthropic).
 */
package es.deusto.sd.auctions.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.deusto.sd.auctions.entity.Article;
import jakarta.persistence.LockModeType;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	// Retrieves an article acquiring a write lock on its row. Used when placing a bid
	// so that concurrent bids on the same article are serialized and the
	// "check current price then register bid" step cannot suffer a lost update.
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from Article a where a.id = :id")
	Optional<Article> findByIdForUpdate(@Param("id") long id);
}
