package it.gionatale.fp.orderservice.domain.basket.representation;

import it.gionatale.fp.orderservice.domain.customer.CustomerId;

import javax.money.MonetaryAmount;
import java.util.List;

public record BasketVO (
        CustomerId customerId,
        List<BasketItemVO> basketItems,
        MonetaryAmount total
){
}
