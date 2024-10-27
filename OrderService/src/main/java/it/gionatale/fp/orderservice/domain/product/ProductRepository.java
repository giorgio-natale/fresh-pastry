package it.gionatale.fp.orderservice.domain.product;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ProductRepository extends CrudRepository<Product, ProductId> {
    Set<Product> findProductsByIdIn(Set<ProductId> productIds);
}
