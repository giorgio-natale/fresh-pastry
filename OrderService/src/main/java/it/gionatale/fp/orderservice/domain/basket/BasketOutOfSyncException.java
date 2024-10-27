package it.gionatale.fp.orderservice.domain.basket;

public class BasketOutOfSyncException extends Exception {
    public BasketOutOfSyncException(String message) {
        super(message);
    }
}
