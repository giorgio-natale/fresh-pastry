package it.gionatale.fp.orderservice.api.representation.out;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import it.gionatale.fp.orderservice.api.representation.ProductDTO;

import java.util.Objects;

public class BasketItemResponseDTO {
    @JsonUnwrapped
    public final ProductDTO product;
    public final int quantity;

    public BasketItemResponseDTO(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketItemResponseDTO that = (BasketItemResponseDTO) o;
        return Objects.equals(product, that.product) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(product);
    }
}
