package hu.webuni.airport.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // profile switcher: prod off or on (test on or off)
public class PriceServiceWithNonProdIT {

    @Autowired
    PriceService priceService;

    @Test
    void testGetFinalPrice() throws Exception {
        int newPrice = priceService.getFinalPrice(100);
        assertThat(newPrice).isEqualTo(90);
    }


    // prod on
    @Test
    void testGetFinalPriceWithHighPrice() throws Exception {
        int newPrice = priceService.getFinalPrice(11000);
        assertThat(newPrice).isEqualTo(9350);
    }

    // prod off
    @Test
    void testGetFinalPriceWithHighPrice2() throws Exception {
        int newPrice = priceService.getFinalPrice(11000);
        assertThat(newPrice).isEqualTo(8800);
    }

}

