package it.gionatale.fp.orderservice.basket;

import it.gionatale.fp.orderservice.domain.basket.Basket;
import it.gionatale.fp.orderservice.domain.basket.BasketId;
import it.gionatale.fp.orderservice.domain.basket.representation.BasketItemVO;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;

import static org.junit.jupiter.api.Assertions.*;

public class AddBasketItemToBasketTest {

    @Test
    public void addItemToEmptyBasket() {
        Basket basket = new Basket(new BasketId(new CustomerId(3L)));
        ProductId productId = new ProductId(1L);
        basket.addItem(new BasketItemVO(productId, Money.of(3, Monetary.getCurrency("EUR")), 1));

        assertEquals(1, basket.getSize());
        assertTrue(basket.getItems().stream().anyMatch(basketItem -> basketItem.getProductId().equals(productId) && basketItem.getQuantity() == 1));
    }
}
