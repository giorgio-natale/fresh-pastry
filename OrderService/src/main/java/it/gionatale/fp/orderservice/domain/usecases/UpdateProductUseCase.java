package it.gionatale.fp.orderservice.domain.usecases;

import it.gionatale.fp.orderservice.api.representation.AmountDTO;
import it.gionatale.fp.orderservice.api.representation.ProductDTO;
import it.gionatale.fp.orderservice.domain.product.Product;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

@Service
public class UpdateProductUseCase {
    private final ProductRepository productRepository;


    public UpdateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDTO updateProduct(ProductId productId, String name, String description, MonetaryAmount price) {
        Product product = productRepository.findById(productId).orElse(new Product(productId, name, description, price));
        productRepository.save(product);
        return new ProductDTO(
                productId.id(),
                product.getName(),
                product.getDescription(),
                new AmountDTO(price.getNumber().numberValue(BigDecimal.class).floatValue(), price.getCurrency().getCurrencyCode())
        );
    }
}
