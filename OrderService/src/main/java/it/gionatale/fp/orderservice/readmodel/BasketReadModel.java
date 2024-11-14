package it.gionatale.fp.orderservice.readmodel;

import it.gionatale.fp.orderservice.api.representation.AmountDTO;
import it.gionatale.fp.orderservice.api.representation.ProductDTO;
import it.gionatale.fp.orderservice.api.representation.out.BasketItemResponseDTO;
import it.gionatale.fp.orderservice.api.representation.out.BasketResponseDTO;
import it.gionatale.fp.orderservice.domain.basket.Basket;
import it.gionatale.fp.orderservice.domain.basket.BasketId;
import it.gionatale.fp.orderservice.domain.basket.BasketItem;
import it.gionatale.fp.orderservice.domain.basket.BasketRepository;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.product.Product;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import it.gionatale.fp.orderservice.domain.usecases.dto.BasketItemRequestVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BasketReadModel {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;

    public BasketReadModel(BasketRepository basketRepository, ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public BasketResponseDTO getBasket(CustomerId customerId) {
        Basket basket = basketRepository.findById(new BasketId(customerId)).orElseThrow();
        MonetaryAmount total = basket.getTotal();
        return new BasketResponseDTO(
                customerId.id(),
                new AmountDTO(total.getNumber().numberValue(BigDecimal.class).floatValue(), total.getCurrency().getCurrencyCode()),
                buildBasketItemResponseDTOs(basket.getItems())
        );
    }

    private List<BasketItemResponseDTO> buildBasketItemResponseDTOs(List<BasketItem> basketItems) {
        Map<ProductId, Product> productsById =
                productRepository.findProductsByIdIn(basketItems.stream().map(BasketItem::getProductId).collect(Collectors.toSet()))
                        .stream()
                        .collect(Collectors.toMap(Product::getId, Function.identity()));

        return basketItems.stream().map(basketItem -> {
            Product product = productsById.get(basketItem.getProductId());
            return new BasketItemResponseDTO(
                new ProductDTO(
                      product.getId().id(),
                      product.getName(),
                      product.getDescription(),
                      new AmountDTO(basketItem.getPrice().getNumber().numberValue(BigDecimal.class).floatValue(), basketItem.getPrice().getCurrency().getCurrencyCode())
                ), basketItem.getQuantity());
            }
        ).toList();
    }
}
