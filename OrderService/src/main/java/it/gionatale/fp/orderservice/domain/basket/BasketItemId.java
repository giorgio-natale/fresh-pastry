package it.gionatale.fp.orderservice.domain.basket;

import it.gionatale.fp.orderservice.domain.product.ProductId;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public record BasketItemId(BasketId basketId, ProductId productId){
    public BasketItemId {
        Assert.notNull(basketId, "BasketItemId basketId must not be null");
        Assert.notNull(productId, "BasketItemId productId must not be null");
    }
}
