package it.gionatale.fp.orderservice.usecases;

import it.gionatale.fp.orderservice.basket.BasketId;
import it.gionatale.fp.orderservice.basket.BasketRepository;
import it.gionatale.fp.orderservice.customer.CustomerId;
import it.gionatale.fp.orderservice.product.ProductId;
import it.gionatale.fp.orderservice.product.ProductRepository;
import org.springframework.stereotype.Service;


@Service
public class ModifyBasketUseCase {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

    public ModifyBasketUseCase(BasketRepository basketRepository, ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
    }

    public void addItemToBasket(CustomerId customerId, ProductId productId) {
        basketRepository.findById(new BasketId(customerId)).ifPresentOrElse(
                basket -> productRepository.findById(productId).ifPresentOrElse(
                        product -> {
                            basket.addItem(product.getId(), product.getCost());
                        },
                        () -> {
                            throw new ProductNotFoundException(String.format("Product '%d' not found", productId.id()));
                        }),
                () -> {throw new CustomerNotFoundException(String.format("Customer '%d' not found", customerId.id()));}
        );
    }
}
