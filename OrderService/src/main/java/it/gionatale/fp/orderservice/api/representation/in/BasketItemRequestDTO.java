package it.gionatale.fp.orderservice.api.representation.in;

public record BasketItemRequestDTO (
        Long productId,
        Integer quantity
){
}
