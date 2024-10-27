package it.gionatale.fp.orderservice.domain.usecases;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
