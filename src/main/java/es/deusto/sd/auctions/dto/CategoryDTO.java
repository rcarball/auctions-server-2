/**
 * This code was originally generated with ChatGPT 4o and adapted using GitHub
 * Copilot. It was reviewed, corrected and updated in July 2026 with the
 * assistance of Claude Opus 4.8 (Anthropic).
 */
package es.deusto.sd.auctions.dto;

public class CategoryDTO {

	private String name;

	// Constructor without parameters
	public CategoryDTO() {
	}

	// Constructor with parameters
	public CategoryDTO(String name) {
		this.name = name;
	}

	// Getters y Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}