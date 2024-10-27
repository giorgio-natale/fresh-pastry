package it.gionatale.fp.orderservice.domain.order;

import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.persistenceconfig.MonetaryAmountUserType;
import jakarta.persistence.*;
import org.hibernate.annotations.CompositeType;

import javax.money.MonetaryAmount;

@Entity
public class OrderItem {

    @EmbeddedId
    @AttributeOverride(name = "orderId.id", column = @Column(name ="order_id"))
    private OrderItemId id;

    @Embedded
    private ProductId productId;

    private int quantity;

    @CompositeType(MonetaryAmountUserType.class)
    private MonetaryAmount cost;

    protected OrderItem() {}

    public OrderItem(OrderId orderId, int orderIndex, ProductId productId, int quantity, MonetaryAmount cost) {
        this.id = new OrderItemId(orderId, orderIndex);
        this.productId = productId;
        this.quantity = quantity;
        this.cost = cost;
    }

    public MonetaryAmount getCost() {
        return cost;
    }

    public OrderItemId getId() {
        return id;
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
