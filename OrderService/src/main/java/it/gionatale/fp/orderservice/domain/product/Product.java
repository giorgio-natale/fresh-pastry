package it.gionatale.fp.orderservice.domain.product;

import it.gionatale.fp.orderservice.persistenceconfig.MonetaryAmountUserType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CompositeType;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

@Entity
public class Product {
    @EmbeddedId
    private ProductId id;

    private String name;

    private String description;

    @CompositeType(MonetaryAmountUserType.class)
    private MonetaryAmount price;

    protected Product() {}

    public Product(ProductId id, String name, String description, MonetaryAmount price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public ProductId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updatePrice(MonetaryAmount newPrice) {
        this.price = newPrice;
    }
}
