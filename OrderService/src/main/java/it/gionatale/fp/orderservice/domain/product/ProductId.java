package it.gionatale.fp.orderservice.domain.product;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProductId (Long id){
}
