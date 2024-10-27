package it.gionatale.fp.orderservice.domain.order;

import it.gionatale.fp.orderservice.domain.basket.BasketItem;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import jakarta.persistence.*;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Order {
    @EmbeddedId
    private OrderId orderId;

    @Embedded
    @AttributeOverride(name="id", column = @Column(name = "customer_id"))
    private CustomerId customerId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", referencedColumnName = "id", updatable = false)
    @OrderBy("order_item_index ASC")
    private List<OrderItem> items;

    private OrderStatus status;

    protected Order() {}

    public Order(OrderId orderId, CustomerId customerId, List<BasketItem> basketItems) {
        this.items = new ArrayList<>();
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = new OrderStatus(OrderStatusName.PENDING, "");

        for (int index = 0; index < basketItems.size(); index++) {
            items.add(new OrderItem(
                                    orderId, index,
                                    basketItems.get(index).getProductId(),
                                    basketItems.get(index).getQuantity(),
                                    basketItems.get(index).getPrice())
            );
        }

    }

    public void updateStatus(OrderStatus status) {
        if (Set.of(OrderStatusName.CONFIRMED, OrderStatusName.REJECTED).contains(this.status.getOrderStatusName())) {
            throwStatusUpdateException(status);
        }
        this.status = status;
    }

    public MonetaryAmount getTotalCost() {
        return this.items.stream()
                         .map(item -> item.getCost().multiply(item.getQuantity()))
                         .reduce(Money.of(0, "EUR"), MonetaryAmount::add);
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    private void throwStatusUpdateException(OrderStatus status) {
        throw new IllegalOrderStatusUpdateException(String.format("order %s changed status from %s to %s", orderId.id(), this.status, status.getOrderStatusName()));
    }
}
