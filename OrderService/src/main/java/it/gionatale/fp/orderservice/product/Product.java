package it.gionatale.fp.orderservice.product;

import it.gionatale.fp.orderservice.persistenceconfig.MonetaryAmountUserType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CompositeType;

import javax.money.MonetaryAmount;

@Entity
public class Product {
    @EmbeddedId
    private ProductId id;

    private String name;

    private String description;

    @CompositeType(MonetaryAmountUserType.class)
    private MonetaryAmount cost;

    protected Product() {}

    public Product(ProductId id, String name, String description, MonetaryAmount cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public MonetaryAmount getCost() {
        return cost;
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
}
