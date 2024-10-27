package it.gionatale.fp.orderservice.domain.basket;

import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
@Access(AccessType.FIELD)
public record BasketId (String id) {
    public BasketId {
        Assert.notNull(id, "id for a basket was null");
    }

    public BasketId(CustomerId customerId) {
        this("Basket#" + customerId.id());
    }

    public CustomerId getCustomerId() {
        return new CustomerId(Long.valueOf(id.split("#")[1]));
    }
}
