package it.gionatale.fp.orderservice.domain.usecases.dto;

import it.gionatale.fp.orderservice.domain.product.ProductId;

public record BasketItemRequestVO(
        ProductId productId,
        int quantity
) {
}
