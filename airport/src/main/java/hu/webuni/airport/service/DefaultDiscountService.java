package hu.webuni.airport.service;


import hu.webuni.airport.config.AirportConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DefaultDiscountService implements DiscountService{

	@Autowired
	AirportConfigProperties config;

	@Override
	public int getDiscountPercent(int totalPrice) {
		return config.getDiscount().getDef().getPercent();
	}

}
