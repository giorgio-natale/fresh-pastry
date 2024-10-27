package it.gionatale.fp.orderservice.domain.basket;

import it.gionatale.fp.orderservice.domain.order.Order;
import it.gionatale.fp.orderservice.domain.order.OrderId;
import it.gionatale.fp.orderservice.domain.product.Product;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import jakarta.persistence.*;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Basket {

    @EmbeddedId
    private BasketId id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name="basket_id", referencedColumnName = "id", updatable = false)
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

    public Order checkout(ProductRepository productRepository) throws BasketOutOfSyncException {
        if (this.items.isEmpty()) {
            throw new EmptyCheckoutException(String.format("tried to checkout with the empty basket %s", this.id.id()));
        }

        boolean isOutOfSync = this.checkIfOutOfSyncAndUpdateItems(productRepository);

        if (isOutOfSync) {
            throw new BasketOutOfSyncException(String.format("basket %s was out of sync", this.id.id()));
        }

        Order order = new Order(new OrderId(UUID.randomUUID()), this.id.getCustomerId(), this.getItems());
        this.items.clear();
        return order;
    }

    public int getSize() {
        return items.size();
    }

    public List<BasketItem> getItems() {
        return new ArrayList<>(items);
    }

    private boolean checkIfOutOfSyncAndUpdateItems(ProductRepository productRepository) {
        Set<ProductId> productIds = this.items.stream().map(BasketItem::getProductId).collect(Collectors.toSet());
        Set<Product> actualProducts = productRepository.findProductsByIdIn(productIds);

        boolean outOfSync = false;
        for (BasketItem basketItem : items) {
            Product actualProduct = actualProducts.stream().filter(product -> product.getId().equals(basketItem.getProductId())).findFirst().orElseThrow();
            if (!actualProduct.getPrice().equals(basketItem.getPrice())) {
                outOfSync = true;
                basketItem.updatePrice(actualProduct.getPrice());
            }
        }

        return outOfSync;
    }
}
