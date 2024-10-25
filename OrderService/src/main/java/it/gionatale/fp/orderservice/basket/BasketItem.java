package it.gionatale.fp.orderservice.basket;

import it.gionatale.fp.orderservice.persistenceconfig.MonetaryAmountUserType;
import it.gionatale.fp.orderservice.product.ProductId;
import jakarta.persistence.*;
import org.hibernate.annotations.CompositeType;

import javax.money.MonetaryAmount;

@Entity
public class BasketItem {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name="basketId.id", column = @Column(name = "basket_id")),
            @AttributeOverride(name="itemId", column = @Column(name = "item_id"))
    })
    private BasketItemId id;

    @Embedded
    @AttributeOverride(name="id", column = @Column(name = "product_id"))
    private ProductId productId;

    private Integer quantity;

    @CompositeType(MonetaryAmountUserType.class)
    private MonetaryAmount cost;


    protected BasketItem() {}

    public BasketItem(ProductId productId, BasketId basketId, Integer index, MonetaryAmount cost) {
        this.productId = productId;
        this.id = new BasketItemId(basketId, index);
        this.cost = cost;
        this.quantity = 1;
    }

    void increaseQuantity() {
        this.quantity++;
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public MonetaryAmount getCost() {
        return cost;
    }
}
