package it.gionatale.fp.orderservice.domain.usecases;

import it.gionatale.fp.orderservice.domain.basket.BasketId;
import it.gionatale.fp.orderservice.domain.basket.BasketRepository;
import it.gionatale.fp.orderservice.domain.basket.representation.BasketItemVO;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class ModifyBasketUseCase {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

    public ModifyBasketUseCase(BasketRepository basketRepository, ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addItemToBasket(CustomerId customerId, ProductId productId) {
        basketRepository.findById(new BasketId(customerId)).ifPresentOrElse(
                basket -> productRepository.findById(productId).ifPresentOrElse(
                        product -> {
                            basket.addItem(new BasketItemVO(product.getId(), product.getPrice(), 1));
                        },
                        () -> {
                            throw new NoSuchElementException(String.format("Product '%d' not found", productId.id()));
                        }),
                () -> {throw new NoSuchElementException(String.format("Customer '%d' not found", customerId.id()));}
        );
    }
}
