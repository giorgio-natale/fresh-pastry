package it.gionatale.fp.orderservice.domain.basket;

import it.gionatale.fp.commons.EntityCollectionUtils;
import it.gionatale.fp.orderservice.domain.basket.representation.BasketItemVO;
import it.gionatale.fp.orderservice.domain.order.Order;
import it.gionatale.fp.orderservice.domain.order.OrderId;
import it.gionatale.fp.orderservice.domain.product.Product;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.product.ProductRepository;
import jakarta.persistence.*;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Basket {

    @EmbeddedId
    private BasketId id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name="basket_id", referencedColumnName = "id", updatable = false)
    @OrderBy("item_order asc")
    private List<BasketItem> items;

    protected Basket(){}

    public Basket(BasketId id) {
        this.id = id;
        items = new ArrayList<>();
    }

    public BasketId getId() {return id;}

    public void addItem(BasketItemVO basketItem) {
        for (BasketItem item : items) {
            if (item.getProductId() == basketItem.productId()) {
                item.increaseQuantity(basketItem.quantity());
                return;
            }
        }
        items.add(new BasketItem(this.id, basketItem.productId(), basketItem.price()));
    }

    public void prepare(List<BasketItemVO> basketItems) {

        Collection<BasketItem> newItems = EntityCollectionUtils.createEntityCollection(
                this.items,
                basketItems,
                BasketItem::getId,
                (BasketItemVO newItem) -> new BasketItemId(this.id, newItem.productId()),
                (BasketItemVO newItem, BasketItem oldItem) -> {
                    oldItem.updateQuantity(newItem.quantity());
                    oldItem.updatePrice(newItem.price());
                },
                (BasketItemVO newItem) -> new BasketItem(this.id, newItem.productId(), newItem.price(), newItem.quantity())
        );
        this.items.clear();
        this.items.addAll(newItems);
        for (int index = 0; index < this.items.size(); index++) {
            this.items.get(index).updateItemOrder(index);
        }

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

    public MonetaryAmount getTotal() {
        return this.items.stream()
                .map(item -> item.getPrice().multiply(item.getQuantity()))
                .reduce(Money.of(0, "EUR"), MonetaryAmount::add);
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
