package it.gionatale.fp.orderservice.domain.order;

import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface OrderRepository extends CrudRepository<Order, OrderId> {
    Set<Order> findAllByCustomerId(CustomerId customerId);
}
