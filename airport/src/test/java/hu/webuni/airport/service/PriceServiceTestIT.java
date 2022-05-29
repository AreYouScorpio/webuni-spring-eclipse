package hu.webuni.airport.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test") // profile switcher: prod off or on (test on or off)
public class PriceServiceTestIT {

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
        assertThat(newPrice).isEqualTo(9900);
    }

}
