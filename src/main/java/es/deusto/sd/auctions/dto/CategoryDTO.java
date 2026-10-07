/**
 * Initially generated with ChatGPT 4o and adapted using GitHub Copilot.
 * Reviewed and updated with assistance from Claude Opus 4.8 (July 2026)
 * and ChatGPT-6.1 Sol (October 2026).
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