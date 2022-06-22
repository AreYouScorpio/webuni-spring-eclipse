package hu.webuni.airport.service;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class AirportServiceIT {

    @Autowired
    AirportService airportService;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    FlightRepository flightRepository;


    @Test
    void testCreateFlight() throws Exception {
        String flightNumber = "AAA";
        //csak hogy legyen az adatbázisban vmi:
        long takeoff = airportRepository.save(new Airport("airport1", "iata1")).getId();
        long landing = airportRepository.save(new Airport("airport2", "iata2")).getId();
        LocalDateTime dateTime = LocalDateTime.now();
        Flight flight = airportService.createFlight(flightNumber, takeoff, landing, dateTime);

        Optional<Flight> savedFlightOptional = flightRepository.findById(flight.getId());
        assertThat(savedFlightOptional).isNotEmpty();
        Flight savedFlight = savedFlightOptional.get();
        assertThat(savedFlight.getFlightNumber()).isEqualTo(flightNumber);
        // chronounit előtt --- > assertThat(savedFlight.getTakeoffTime()).isEqualTo(dateTime);
        // de eltérhet ahogyan a DB tárolja a dateTime hosszát attól, ahogy itt létrehoztuk, ezért:

        // chronounit után (ha eltérne a DB és a programban létrehozott dateTime tárolásának hossza)
        assertThat(savedFlight.getTakeoffTime())
                .isCloseTo(dateTime,
                        new TemporalUnitWithinOffset(1, ChronoUnit.MICROS));

        // lehetne rövidebben, szebben ugyanezt Assertion-nel:
        assertThat(savedFlight.getTakeoffTime())
                .isCloseTo(dateTime, Assertions.within(1, ChronoUnit.MICROS));

        assertThat(savedFlight.getTakeoff().getId()).isEqualTo(takeoff);
        assertThat(savedFlight.getLanding().getId()).isEqualTo(landing);
    }



}
