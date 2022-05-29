package hu.webuni.airport.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceServiceTest {

    @Test
    void testGetFinalPrice() throws Exception {
        int newPrice = new PriceService(p->5).getFinalPrice(100);
        assertEquals(95, newPrice);
    }

}
