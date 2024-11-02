package it.gionatale.fp.orderservice.basket;

import it.gionatale.fp.orderservice.domain.basket.*;
import it.gionatale.fp.orderservice.domain.basket.representation.BasketItemVO;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.product.Product;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import it.gionatale.fp.orderservice.domain.usecases.ModifyBasketUseCase;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import javax.money.Monetary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddBasketItemToBasketJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void createEmptyBasket() {
        entityManager.persist(new Basket(new BasketId(new CustomerId(8L))));

        assertEquals(0, entityManager.find(Basket.class, new BasketId(new CustomerId(8L))).getSize());
    }

    @Test
    public void addItemToEmptyBasket() {
        CustomerId customerId = new CustomerId(8L);
        BasketId basketId = new BasketId(customerId);

        Basket basket = entityManager.persist(new Basket(basketId));

        ProductId productId = new ProductId(1L);
        entityManager.persist(new Product(productId, "Choco Pie", "Awesome chocolate pie", Money.of(3, Monetary.getCurrency("EUR"))));

        ModifyBasketUseCase modifyBasketUseCase = new ModifyBasketUseCase(basketRepository, productRepository);
        modifyBasketUseCase.addItemToBasket(customerId, productId);
        entityManager.flush();

        Basket updatedBasket = entityManager.find(Basket.class, basket.getId());
        assertEquals(basket, updatedBasket);

        BasketItem chocoPie = entityManager.find(BasketItem.class, new BasketItemId(basketId, productId));
        assertEquals(Money.of(3, Monetary.getCurrency("EUR")), chocoPie.getPrice());
        assertEquals(1, chocoPie.getQuantity());
        assertEquals(productId, chocoPie.getProductId());

    }

    @Test
    public void addItemToBasketTwice() {
        CustomerId customerId = new CustomerId(8L);
        BasketId basketId = new BasketId(customerId);

        Basket basket = entityManager.persist(new Basket(basketId));

        ProductId productId = new ProductId(1L);
        entityManager.persist(new Product(productId, "Choco Pie", "Awesome chocolate pie", Money.of(3, Monetary.getCurrency("EUR"))));

        ModifyBasketUseCase modifyBasketUseCase = new ModifyBasketUseCase(basketRepository, productRepository);
        modifyBasketUseCase.addItemToBasket(customerId, productId);
        modifyBasketUseCase.addItemToBasket(customerId, productId);
        entityManager.flush();

        Basket updatedBasket = entityManager.find(Basket.class, basket.getId());
        assertEquals(basket, updatedBasket);

        BasketItem chocoPie = entityManager.find(BasketItem.class, new BasketItemId(basketId, productId));
        assertEquals(Money.of(3, Monetary.getCurrency("EUR")), chocoPie.getPrice());
        assertEquals(2, chocoPie.getQuantity());
        assertEquals(productId, chocoPie.getProductId());
    }

    @Test
    public void addTwoDifferentItemsToBasket() {
        CustomerId customerId = new CustomerId(8L);
        BasketId basketId = new BasketId(customerId);

        Basket basket = entityManager.persist(new Basket(basketId));

        ProductId productId1 = new ProductId(1L);
        ProductId productId2 = new ProductId(2L);

        entityManager.persist(new Product(productId1, "Choco Pie", "Awesome chocolate pie", Money.of(3, Monetary.getCurrency("EUR"))));
        entityManager.persist(new Product(productId2, "Apple Pie", "Awesome apple pie", Money.of(2, Monetary.getCurrency("EUR"))));

        ModifyBasketUseCase modifyBasketUseCase = new ModifyBasketUseCase(basketRepository, productRepository);
        modifyBasketUseCase.addItemToBasket(customerId, productId1);
        modifyBasketUseCase.addItemToBasket(customerId, productId2);
        entityManager.flush();

        Basket updatedBasket = entityManager.find(Basket.class, basket.getId());

        assertEquals(2, updatedBasket.getSize());
        assertEquals(productId2, updatedBasket.getItems().get(1).getProductId());
        assertTrue(updatedBasket.getItems().stream().allMatch(item -> item.getQuantity() == 1));
    }

    @Test
    public void prepareBasketAfterItAlreadyHadSomeItems() {
        CustomerId customerId = new CustomerId(8L);
        BasketId basketId = new BasketId(customerId);

        Basket basket = entityManager.persist(new Basket(basketId));

        ProductId productId1 = new ProductId(1L);
        ProductId productId2 = new ProductId(2L);
        ProductId productId3 = new ProductId(3L);


        entityManager.persist(new Product(productId1, "Choco Pie", "Awesome chocolate pie", Money.of(3, Monetary.getCurrency("EUR"))));
        entityManager.persist(new Product(productId2, "Apple Pie", "Awesome apple pie", Money.of(2, Monetary.getCurrency("EUR"))));
        entityManager.persist(new Product(productId3, "Orange Pie", "Awesome orange pie", Money.of(2, Monetary.getCurrency("EUR"))));

        basket.prepare(List.of(new BasketItemVO(productId1, Money.of(2, "EUR"), 2), new BasketItemVO(productId2, Money.of(2, "EUR"), 1)));

        entityManager.flush();

        basket.prepare(List.of(new BasketItemVO(productId3, Money.of(500, "EUR"), 2), new BasketItemVO(productId1, Money.of(2, "EUR"), 1)));

        entityManager.flush();

        assertEquals(List.of(productId3, productId1), basket.getItems().stream().map(BasketItem::getProductId).toList());
        assertEquals(1, basket.getItems().get(1).getQuantity());
        assertEquals(Money.of(500, "EUR"), basket.getItems().get(0).getPrice());
    }

}
