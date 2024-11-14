package it.gionatale.fp.orderservice.api.representation;

public record ProductDTO (
        Long productId,
        String name,
        String description,
        AmountDTO price
){

}
