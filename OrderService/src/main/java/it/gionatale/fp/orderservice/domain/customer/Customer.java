package it.gionatale.fp.orderservice.domain.customer;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Customer {
    @EmbeddedId
    private CustomerId id;
}
