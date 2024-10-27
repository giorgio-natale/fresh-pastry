package it.gionatale.fp.orderservice.basket;

import it.gionatale.fp.orderservice.domain.basket.Basket;
import it.gionatale.fp.orderservice.domain.basket.BasketId;
import it.gionatale.fp.orderservice.domain.basket.BasketOutOfSyncException;
import it.gionatale.fp.orderservice.domain.basket.BasketRepository;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.order.OrderRepository;
import it.gionatale.fp.orderservice.domain.order.OrderStatusName;
import it.gionatale.fp.orderservice.domain.product.Product;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import it.gionatale.fp.orderservice.domain.usecases.CheckoutUseCase;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import it.gionatale.fp.orderservice.domain.order.Order;

import javax.money.Monetary;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CheckoutJpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void checkoutBasket() throws BasketOutOfSyncException {
        CustomerId customerId = new CustomerId(8L);
        BasketId basketId = new BasketId(customerId);

        Basket basket = entityManager.persist(new Basket(basketId));

        ProductId productId = new ProductId(1L);
        Product product = entityManager.persist(new Product(productId, "Choco Pie", "Awesome chocolate pie", Money.of(3, Monetary.getCurrency("EUR"))));

        basket.addItem(productId, product.getPrice());

        entityManager.flush();

        Order order = basket.checkout(productRepository);

        assertEquals(product.getPrice(), order.getTotalCost());
        assertEquals(product.getId(), order.getItems().get(0).getProductId());
        assertEquals(0, basket.getSize());

        entityManager.flush();

    }

    @Test
    public void checkoutBasketWithOutOfDateItemUseCase() {
        CustomerId customerId = new CustomerId(8L);
        BasketId basketId = new BasketId(customerId);

        Basket basket = entityManager.persist(new Basket(basketId));

        ProductId productId = new ProductId(1L);
        Product product = entityManager.persist(new Product(productId, "Choco Pie", "Awesome chocolate pie", Money.of(3, Monetary.getCurrency("EUR"))));

        basket.addItem(productId, product.getPrice());

        entityManager.flush();

        product.updatePrice(Money.of(5_000, "EUR"));

        entityManager.flush();

        CheckoutUseCase checkoutUseCase = new CheckoutUseCase(basketRepository, orderRepository, productRepository);
        assertThrows(BasketOutOfSyncException.class, () -> checkoutUseCase.checkout(customerId));

        entityManager.flush();

        assertEquals(product.getPrice(), basket.getItems().get(0).getPrice());
    }

    @Test
    public void checkoutBasketUseCase() throws BasketOutOfSyncException {
        CustomerId customerId = new CustomerId(8L);
        BasketId basketId = new BasketId(customerId);

        Basket basket = entityManager.persist(new Basket(basketId));

        ProductId productId = new ProductId(1L);
        Product product = entityManager.persist(new Product(productId, "Choco Pie", "Awesome chocolate pie", Money.of(3, Monetary.getCurrency("EUR"))));

        basket.addItem(productId, product.getPrice());

        entityManager.flush();

        CheckoutUseCase checkoutUseCase = new CheckoutUseCase(basketRepository, orderRepository, productRepository);
        checkoutUseCase.checkout(customerId);

        entityManager.flush();

        Order order = orderRepository.findAllByCustomerId(customerId).iterator().next();

        entityManager.flush();

        assertEquals(product.getPrice(), order.getItems().get(0).getCost());
        assertEquals(product.getId(), order.getItems().get(0).getProductId());
        assertEquals(OrderStatusName.PENDING, order.getStatus().getOrderStatusName());
        assertEquals(0, basket.getSize());

    }
}


