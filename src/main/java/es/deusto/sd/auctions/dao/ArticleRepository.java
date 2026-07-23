/**
 * This code is based on solutions provided by ChatGPT 4o and
 * adapted using GitHub Copilot. It has been thoroughly reviewed
 * and validated to ensure correctness and that it is free of errors.
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
