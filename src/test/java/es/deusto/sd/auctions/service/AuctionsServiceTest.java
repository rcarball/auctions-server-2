package es.deusto.sd.auctions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AuctionsServiceTest {

    // The invalid amount is rejected before either repository is consulted.
    private final AuctionsService service = new AuctionsService(null, null);

    @Test
    void rejectsNaNAsABidAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.makeBid(null, 1L, Double.NaN));

        assertEquals("Bid amount must be a finite positive number", exception.getMessage());
    }

    @Test
    void rejectsInfiniteAndNonPositiveBidAmounts() {
        assertThrows(IllegalArgumentException.class,
                () -> service.makeBid(null, 1L, Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class,
                () -> service.makeBid(null, 1L, 0.0));
    }
}
