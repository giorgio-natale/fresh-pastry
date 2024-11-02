package it.gionatale.fp.orderservice.domain.basket.representation;

import it.gionatale.fp.orderservice.domain.product.ProductId;
import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;

public record BasketItemVO(ProductId productId, MonetaryAmount price, int quantity) {
    public BasketItemVO {
        Assert.notNull(productId, "productId must not be null");
        Assert.notNull(price, "price must not be null");
        Assert.isTrue(quantity > 0, "quantity must be greater than zero");
        Assert.isTrue(price.isGreaterThan(Money.of(0, "EUR")), "price must be greater than zero");
    }
}
