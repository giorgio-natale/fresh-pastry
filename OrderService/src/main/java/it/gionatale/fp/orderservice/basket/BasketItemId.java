package it.gionatale.fp.orderservice.basket;

import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public record BasketItemId(BasketId basketId, Integer itemId){
    public BasketItemId {
        Assert.notNull(basketId, "BasketItemId basketId must not be null");
        Assert.isTrue(itemId >= 0, "BasketItemId itemId must be greater than 0");
    }
}
