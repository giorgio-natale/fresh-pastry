package it.gionatale.fp.orderservice.domain.basket;

public class EmptyCheckoutException extends RuntimeException {
    public EmptyCheckoutException(String message) {
        super(message);
    }
}
