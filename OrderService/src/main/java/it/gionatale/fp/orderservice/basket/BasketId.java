package it.gionatale.fp.orderservice.basket;

import it.gionatale.fp.orderservice.customer.CustomerId;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public record BasketId (String id) {
    public BasketId {
        Assert.notNull(id, "id for a basket was null");
    }

    public BasketId(CustomerId customerId) {
        this("Basket#" + customerId.id());
    }
}
