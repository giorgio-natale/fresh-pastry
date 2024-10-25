package it.gionatale.fp.orderservice.basket;

import it.gionatale.fp.orderservice.customer.CustomerId;
import it.gionatale.fp.orderservice.product.ProductId;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;

import static org.junit.jupiter.api.Assertions.*;

public class AddBasketItemToBasketTest {

    @Test
    public void addItemToEmptyBasket() {
        Basket basket = new Basket(new BasketId(new CustomerId(3L)));
        ProductId productId = new ProductId(1L);
        basket.addItem(productId, Money.of(3, Monetary.getCurrency("EUR")));

        assertEquals(1, basket.getSize());
        assertTrue(basket.getItems().stream().anyMatch(basketItem -> basketItem.getProductId().equals(productId) && basketItem.getQuantity() == 1));
    }
}
