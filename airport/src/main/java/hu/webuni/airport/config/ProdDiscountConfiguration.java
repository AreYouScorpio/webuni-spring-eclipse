package hu.webuni.airport.config;

import hu.webuni.airport.service.DefaultDiscountService;
import hu.webuni.airport.service.DiscountService;
import hu.webuni.airport.service.SpecialDiscountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdDiscountConfiguration {


    @Bean
    public DiscountService discountService() {
        return new SpecialDiscountService();
    }


}
