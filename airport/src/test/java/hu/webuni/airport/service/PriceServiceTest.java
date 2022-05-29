package hu.webuni.airport.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {

    @InjectMocks
    PriceService priceService;

    @Mock
    DiscountService discountService;
    /*

    // unit5 jupiter test:

    @Test
    void testGetFinalPrice() throws Exception {
        int newPrice = new PriceService(p->5).getFinalPrice(100);
        // assertj-re cseréljük jupitert:
        // assertEquals(95, newPrice);

        assertThat(newPrice).isEqualTo(95);
    }


     */

    // Mockito test:

    @Test
    void testGetFinalPrice() throws Exception {

        Mockito.when(discountService.getDiscountPercent(100)).thenReturn(5);
        int newPrice = priceService.getFinalPrice(100);
        // assertj-re cseréljük jupitert:
        // assertEquals(95, newPrice);

        assertThat(newPrice).isEqualTo(95);
    }


}
