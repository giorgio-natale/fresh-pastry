package it.gionatale.fp.orderservice.domain.order;

import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

import java.util.UUID;

@Embeddable
public record OrderId (UUID id) {
    public OrderId {
        Assert.notNull(id, "order id must not be null");
    }
}
