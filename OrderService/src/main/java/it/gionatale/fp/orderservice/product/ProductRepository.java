package it.gionatale.fp.orderservice.product;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, ProductId> {
}
