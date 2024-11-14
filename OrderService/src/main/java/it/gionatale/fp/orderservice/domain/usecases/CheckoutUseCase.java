package it.gionatale.fp.orderservice.domain.usecases;

import it.gionatale.fp.orderservice.domain.basket.Basket;
import it.gionatale.fp.orderservice.domain.basket.BasketId;
import it.gionatale.fp.orderservice.domain.basket.BasketOutOfSyncException;
import it.gionatale.fp.orderservice.domain.basket.BasketRepository;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.order.Order;
import it.gionatale.fp.orderservice.domain.order.OrderRepository;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CheckoutUseCase {
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public CheckoutUseCase(BasketRepository basketRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    @Transactional(dontRollbackOn = BasketOutOfSyncException.class)
    public void checkout(CustomerId customerId) throws BasketOutOfSyncException {
        Basket basket = basketRepository.findById(new BasketId(customerId)).orElseThrow(() -> new NoSuchElementException(String.format("Customer with id %d not found", customerId.id())));
        Order order = basket.checkout(productRepository);
        orderRepository.save(order);
    }
}
