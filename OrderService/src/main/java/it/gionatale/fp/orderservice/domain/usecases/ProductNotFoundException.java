package it.gionatale.fp.orderservice.domain.usecases;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

}
