/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // "user" is a reserved keyword in H2 database
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    // One-to-many relationship with Bid entity
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Bid> bids = new ArrayList<>();

    // One-to-many relationship with Article entity
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Article> articles = new ArrayList<>();

	// Constructor without parameters
	public User() { }
	
	// Constructor with parameters
	public User(String nickname, String email, String password) {
		this.nickname = nickname;
		this.email = email;
		// Passwords are never stored in plain text: they are hashed with SHA-1
		// before being persisted (see the "Login" scenario in the case study).
		this.password = DigestUtils.sha1Hex(password);
	}

	// Check if a password is correct by comparing the SHA-1 hash of the provided
	// password against the stored hash (the plain-text password is never kept).
	public boolean checkPassword(String password) {
        return this.password.equals(DigestUtils.sha1Hex(password));
	}

	//  Getters and setters
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	// Note: there is no getPassword() on purpose. As an additional security measure
	// the stored password (a SHA-1 hash) is never exposed outside the class.

	public void setPassword(String password) {
		this.password = DigestUtils.sha1Hex(password);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	// hashCode and equals based on the natural business key (email), which is unique
	// and non-null. Using the auto-generated id here would be unsafe: it is null until
	// the entity is persisted and changes on insert, breaking hash-based collections.
	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email);
	}
}