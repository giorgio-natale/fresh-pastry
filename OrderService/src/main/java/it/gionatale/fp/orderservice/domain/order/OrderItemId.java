package it.gionatale.fp.orderservice.domain.order;

import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public record OrderItemId (OrderId orderId, Integer orderItemIndex) {
    public OrderItemId {
        Assert.notNull(orderId, "OrderItemId orderId must not be null");
        Assert.isTrue(orderItemIndex >= 0, "OrderItemId orderItemIndex must be greater than 0");
    }
}
