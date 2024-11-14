package it.gionatale.fp.orderservice.basket;

import it.gionatale.fp.orderservice.api.ed.CustomerController;
import it.gionatale.fp.orderservice.api.representation.AmountDTO;
import it.gionatale.fp.orderservice.api.representation.ProductDTO;
import it.gionatale.fp.orderservice.api.representation.in.BasketItemRequestDTO;
import it.gionatale.fp.orderservice.api.representation.in.BasketUpdateRequestDTO;
import it.gionatale.fp.orderservice.api.representation.out.BasketResponseDTO;
import it.gionatale.fp.orderservice.api.rest.BasketController;
import it.gionatale.fp.orderservice.api.rest.ProductController;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PrepareBasketE2ETest {
    @Autowired
    private BasketController basketController;

    @Autowired
    private ProductController productController;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void prepareEmptyBasket() {
        productController.updateProduct(0L, new ProductDTO(
                0L,
                "Apple Pie",
                "An apple pie",
                new AmountDTO(
                        3.0f, "EUR"
                )
        ));

        customerController.registerCustomer(1L);

        entityManager.flush();
    }

    @Test
    @Transactional
    public void prepareBasketWithSomeProducts() {
        productController.updateProduct(0L, new ProductDTO(
                0L,
                "Apple Pie",
                "An apple pie",
                new AmountDTO(
                        3.0f, "EUR"
                )
        ));

        productController.updateProduct(1L, new ProductDTO(
                1L,
                "Chocolate Pie",
                "A Chocolate pie",
                new AmountDTO(
                        5.0f, "EUR"
                )
        ));

        customerController.registerCustomer(1L);


        BasketResponseDTO basketResponseDTO = basketController.prepareBasket(1L, new BasketUpdateRequestDTO(
                1L, List.of(new BasketItemRequestDTO(0L, 2), new BasketItemRequestDTO(1L, 3))
        ));


        assertEquals(21.0f, basketResponseDTO.total().amount().floatValue());
    }


}
