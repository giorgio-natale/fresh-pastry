package it.gionatale.fp.orderservice.domain.usecases;

import it.gionatale.fp.orderservice.domain.basket.Basket;
import it.gionatale.fp.orderservice.domain.basket.BasketId;
import it.gionatale.fp.orderservice.domain.basket.BasketRepository;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterCustomerUseCase {
    private final BasketRepository basketRepository;

    public RegisterCustomerUseCase(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Transactional
    public void registerCustomer(CustomerId customerId) {
        BasketId basketId = new BasketId(customerId);
        if(!basketRepository.existsById(basketId)) {
            basketRepository.save(new Basket(basketId));
        }
    }
}
