package it.gionatale.fp.orderservice.api.rest;

import it.gionatale.fp.orderservice.api.representation.in.BasketUpdateRequestDTO;
import it.gionatale.fp.orderservice.api.representation.out.BasketResponseDTO;
import it.gionatale.fp.orderservice.domain.customer.CustomerId;
import it.gionatale.fp.orderservice.domain.product.ProductId;
import it.gionatale.fp.orderservice.domain.usecases.BasketUseCase;
import it.gionatale.fp.orderservice.domain.usecases.dto.BasketItemRequestVO;
import it.gionatale.fp.orderservice.readmodel.BasketReadModel;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BasketController {

    private final BasketUseCase basketUsecase;
    private final BasketReadModel basketReadModel;
    private final TransactionTemplate transactionTemplate;

    public BasketController(BasketUseCase basketUsecase, BasketReadModel basketReadModel, PlatformTransactionManager transactionManager) {
        this.basketUsecase = basketUsecase;
        this.basketReadModel = basketReadModel;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @PutMapping("/baskets/{customerId}")
    public BasketResponseDTO prepareBasket(@PathVariable Long customerId, BasketUpdateRequestDTO basketUpdateRequestDTO) {
        List<BasketItemRequestVO> basketItemRequestVOs = basketUpdateRequestDTO.basketItems()
                .stream()
                .map(itemDTO -> new BasketItemRequestVO(new ProductId(itemDTO.productId()), itemDTO.quantity()))
                .toList();

        return transactionTemplate.execute((status) -> {
            basketUsecase.prepareBasket(new CustomerId(customerId), basketItemRequestVOs);
            return basketReadModel.getBasket(new CustomerId(customerId));
        });
    }
}
