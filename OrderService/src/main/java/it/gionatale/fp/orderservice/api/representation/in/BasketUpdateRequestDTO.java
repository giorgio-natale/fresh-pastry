package it.gionatale.fp.orderservice.api.representation.in;

import it.gionatale.fp.orderservice.api.representation.out.BasketItemResponseDTO;

import java.util.List;

public record BasketUpdateRequestDTO (
        Long customerId,
        List<BasketItemRequestDTO> basketItems
) {
}
