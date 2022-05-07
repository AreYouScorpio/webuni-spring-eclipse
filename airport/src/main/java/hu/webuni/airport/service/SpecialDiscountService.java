package hu.webuni.airport.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SpecialDiscountService implements DiscountService{

    /*
    @Value ("${airport.discount.special.limit}")
    private int limit;

    @Value ("${airport.discount.default.percent}")
    private int defaultPercent;

    @Value ("${airport.discount.special.percent}")
    private int specialPercent;


     */
    @Override
    public int getDiscountPercent(int totalPrice) {
        return totalPrice>limit ? specialPercent :defaultPercent;
    }
}
