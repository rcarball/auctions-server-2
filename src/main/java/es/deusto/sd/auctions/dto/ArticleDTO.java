/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions.dto;

import java.util.Date;

public class ArticleDTO {
    private long id;
    private String title;
    private double initialPrice;
    private double currentPrice;
    private int bids;
    private Date auctionEnd;
    private String categoryName;
    private String ownerName;
    private String currency;
    
    // Constructor without parameters
	public ArticleDTO() { }
    
    // Constructor with parameters
    public ArticleDTO(long id, String title, double initialPrice, double currentPrice, int bids,
    				  Date auctionEnd, String categoryName, String ownerName, String currency) {
        this.id = id;
        this.title = title;
        this.initialPrice = initialPrice;
        this.currentPrice = currentPrice;
        this.bids = bids;
        this.auctionEnd = auctionEnd;
        this.categoryName = categoryName;
        this.ownerName = ownerName;
        this.currency = currency;
    }

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public int getBids() {
		return bids;
	}
	
	public void setBids(int bids) {
		this.bids = bids;
	}
    
    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}