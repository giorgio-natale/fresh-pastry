package it.gionatale.fp.orderservice.api.representation.out;

import it.gionatale.fp.orderservice.api.representation.AmountDTO;

import java.util.List;

public record BasketResponseDTO(
        Long customerId,
        AmountDTO total,
        List<BasketItemResponseDTO> basketItems
){
}
