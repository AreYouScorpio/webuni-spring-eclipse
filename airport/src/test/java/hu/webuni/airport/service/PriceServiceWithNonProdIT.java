package hu.webuni.airport.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

// a target / test classes / application-test.properties értékeiben csak ez szerepeljen
// (minden indítás után felülíródik):
// airport.discount.def.percent=10
//        airport.discount.special.percent=15
//        airport.discount.special.limit=10000


@SpringBootTest
@ActiveProfiles("test") // profile switcher: prod off or on (test on or off)
public class PriceServiceWithNonProdIT {

    @Autowired
    PriceService priceService;

    @Test
    void testGetFinalPrice() throws Exception {
        int newPrice = priceService.getFinalPrice(100);
        assertThat(newPrice).isEqualTo(80);
    }


    // prod on - prod off now == 10% should be okay if settings for "prod" on, 10%
    // beware other properties file for test
    @Test
    void testGetFinalPriceWithHighPrice() throws Exception {
        int newPrice = priceService.getFinalPrice(9000);
        assertThat(newPrice).isEqualTo(8100);
    }

    // prod off / test on 8800 - 20% should be okay if settings for "test" on (prod off)
    @Test
    void testGetFinalPriceWithHighPrice2() throws Exception {
        int newPrice = priceService.getFinalPrice(11000);
        assertThat(newPrice).isEqualTo(8800);
    }

}

