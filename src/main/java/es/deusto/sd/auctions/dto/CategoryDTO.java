/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
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