package it.gionatale.fp.orderservice.api.ed;

import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.usecases.RegisterCustomerUseCase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomerController {
    private final RegisterCustomerUseCase registerCustomerUseCase;

    public CustomerController(RegisterCustomerUseCase registerCustomerUseCase) {
        this.registerCustomerUseCase = registerCustomerUseCase;
    }

    //TODO: make this a listener
    @Transactional
    public void registerCustomer(Long customerId) {
        registerCustomerUseCase.registerCustomer(new CustomerId(customerId));
    }
}
