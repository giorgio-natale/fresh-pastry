package it.gionatale.fp.orderservice.domain.basket;

import it.gionatale.fp.orderservice.persistenceconfig.MonetaryAmountUserType;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import jakarta.persistence.*;
import org.hibernate.annotations.CompositeType;

import javax.money.MonetaryAmount;
import java.util.Objects;

@Entity
public class BasketItem {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name="basketId.id", column = @Column(name = "basket_id")),
            @AttributeOverride(name="productId.id", column = @Column(name = "product_id"))
    })
    private BasketItemId id;

    private Integer quantity;

    @CompositeType(MonetaryAmountUserType.class)
    private MonetaryAmount price;

    private int itemOrder;

    protected BasketItem() {}

    public BasketItem(BasketId basketId, ProductId productId, MonetaryAmount price) {
        this.id = new BasketItemId(basketId, productId);
        this.price = price;
        this.quantity = 1;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    void updateItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }

    public BasketItemId getId() {
        return id;
    }

    public ProductId getProductId() {
        return id.productId();
    }

    public int getQuantity() {
        return quantity;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public void updatePrice(MonetaryAmount price) {
        this.price = price;
    }
}
