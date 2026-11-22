package es.deusto.sd.auctions.service;

public class AuctionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public enum Reason {
        NOT_FOUND,
        INVALID_AMOUNT,
        BID_TOO_LOW,
        AUCTION_ENDED
    }

    private final Reason reason;

    public AuctionException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
