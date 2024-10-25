package it.gionatale.fp.orderservice.basket;

import it.gionatale.fp.orderservice.product.ProductId;
import jakarta.persistence.*;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Basket {

    @EmbeddedId
    private BasketId id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="basket_id", referencedColumnName = "id")
    @OrderBy("item_id ASC")
    private List<BasketItem> items;

    protected Basket(){}

    public Basket(BasketId id) {
        this.id = id;
        items = new ArrayList<>();
    }

    public BasketId getId() {return id;}

    public void addItem(ProductId productId, MonetaryAmount cost) {
        for (BasketItem item : items) {
            if (item.getProductId() == productId) {
                item.increaseQuantity();
                return;
            }
        }
        items.add(new BasketItem(productId, this.id, items.size(), cost));
    }

    public int getSize() {
        return items.size();
    }

    public List<BasketItem> getItems() {
        return new ArrayList<>(items);
    }
}
