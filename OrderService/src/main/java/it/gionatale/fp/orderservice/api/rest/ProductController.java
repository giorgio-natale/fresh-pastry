package it.gionatale.fp.orderservice.api.rest;

import it.gionatale.fp.orderservice.api.representation.ProductDTO;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.usecases.UpdateProductUseCase;
import org.javamoney.moneta.Money;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final UpdateProductUseCase updateProductUseCase;
    private final TransactionTemplate transactionTemplate;

    public ProductController(UpdateProductUseCase updateProductUseCase, PlatformTransactionManager transactionManager) {
        this.updateProductUseCase = updateProductUseCase;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @PutMapping("/products/{productId}")
    public ProductDTO updateProduct(@PathVariable Long productId, ProductDTO productDTO) {
        return transactionTemplate.execute(status ->

                updateProductUseCase.updateProduct(
                    new ProductId(productId),
                    productDTO.name(),
                    productDTO.description(),
                    Money.of(productDTO.price().amount(), "EUR")
                )

        );
    }
}
