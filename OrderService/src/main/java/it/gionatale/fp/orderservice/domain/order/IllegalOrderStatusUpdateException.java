package it.gionatale.fp.orderservice.domain.order;

public class IllegalOrderStatusUpdateException extends RuntimeException {
    public IllegalOrderStatusUpdateException(String message) {
        super(message);
    }
}
