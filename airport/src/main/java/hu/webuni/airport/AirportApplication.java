package hu.webuni.airport;

import hu.webuni.airport.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
//@SpringBootApplication
//ha security nélkül akarjuk futtatni:
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AirportApplication implements CommandLineRunner{

	@Autowired
	PriceService priceService;

	@Autowired
	AirportService airportService;

	@Autowired
	InitDbService initDbService;

	public static void main(String[] args) {
		SpringApplication.run(AirportApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		// airportService.createFlight();
		System.out.println(priceService.getFinalPrice(200));
		System.out.println(priceService.getFinalPrice(20000));

//		initDbService.createUsersIfNeeded();

		initDbService.deleteDb();
		initDbService.addInitData();
	}



}
