package it.gionatale.fp.orderservice.domain.usecases;

import it.gionatale.fp.orderservice.domain.basket.Basket;
import it.gionatale.fp.orderservice.domain.basket.BasketId;
import it.gionatale.fp.orderservice.domain.basket.BasketRepository;
import it.gionatale.fp.orderservice.domain.basket.representation.BasketItemVO;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.product.Product;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import it.gionatale.fp.orderservice.domain.usecases.dto.BasketItemRequestVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class BasketUseCase {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

    public BasketUseCase(BasketRepository basketRepository, ProductRepository productRepository) {
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

    @Transactional
    public void prepareBasket(CustomerId customerId, List<BasketItemRequestVO> basketItemRequestVOs) {
        Basket basket = basketRepository.findById(new BasketId(customerId)).orElseThrow(() ->
                new NoSuchElementException(String.format("User '%d' not found", customerId.id())));

        List<BasketItemVO> basketItemVOs = buildBasketItemsVO(basketItemRequestVOs, productRepository);

        basket.prepare(basketItemVOs);
    }


    private List<BasketItemVO> buildBasketItemsVO(List<BasketItemRequestVO> basketItemRequestVOs, ProductRepository productRepository) {
        Map<ProductId, Product> productsById =
                productRepository.findProductsByIdIn(basketItemRequestVOs.stream().map(BasketItemRequestVO::productId).collect(Collectors.toSet()))
                        .stream()
                        .collect(Collectors.toMap(Product::getId, Function.identity()));

        return basketItemRequestVOs.stream().map(itemRequestVO -> new BasketItemVO(
                itemRequestVO.productId(),
                productsById.get(itemRequestVO.productId()).getPrice(),
                itemRequestVO.quantity()
        )).toList();
    }
}
