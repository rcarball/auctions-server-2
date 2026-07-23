/**
 * This code was originally generated with ChatGPT 4o and adapted using GitHub
 * Copilot. It was reviewed, corrected and updated in July 2026 with the
 * assistance of Claude Opus 4.8 (Anthropic).
 */
package es.deusto.sd.auctions.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.auctions.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByName(String name);
}