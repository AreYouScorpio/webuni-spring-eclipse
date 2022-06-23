package hu.webuni.airport.service;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        long takeoff = createAirport("airport1", "iata1");
        long landing = createAirport("airport2", "iata2");
        // kiszerveztük createAirportba, ezt az eredetit:
        // long landing = airportRepository.save(new Airport("airport2", "iata2")).getId();
        LocalDateTime dateTime = LocalDateTime.now();
        //ez is kiszervezve
        // Flight flight = airportService.createFlight(flightNumber, takeoff, landing, dateTime);
        // így az új:
        // a teszthez aztán mégis iknább longot adtunk vissza:
        // ehelyett ... Flight flight = createFlight(flightNumber, takeoff, landing, dateTime);
        // új:
        long flightId = createFlight(flightNumber, takeoff, landing, dateTime);
        // és akkor persze ehelyett is más kell:
        // Optional<Flight> savedFlightOptional = flightRepository.findById(flight.getId());
        // új:
        Optional<Flight> savedFlightOptional = flightRepository.findById(flightId);
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

    private long createAirport(String name, String iata) {
        return airportRepository.save(
                new Airport(name, iata)).getId();
    }

    @Test
    void testFindFlightsByExample() throws Exception {

        long airport1Id = createAirport("airport1", "iata1");
        long airport2Id = createAirport("airport2", "iata2");
        long airport3Id = createAirport("airport3", "2iata");
        long airport4Id = createAirport("airport4", "3ata4");

        LocalDateTime takeoff = LocalDateTime.of(
                2021, 4 , 23, 8, 0, 0);

        long flight1 = createFlight("ABC123", airport1Id, airport3Id, takeoff);
        long flight2 = createFlight("ABC1234", airport2Id, airport3Id, takeoff.plusHours(2));
        createFlight("BC123", airport1Id, airport3Id, takeoff);
        createFlight("ABC123", airport1Id, airport3Id, takeoff.plusDays(1));
        createFlight("ABC123", airport3Id, airport3Id, takeoff);

        Flight example = new Flight();
        example.setFlightNumber("ABC123");
        example.setTakeoffTime(takeoff);
        example.setTakeoff(new Airport("sasa", "iata"));

        List<Flight> foundFlights =
            this.airportService.findFlightsByExample(example);

        assertThat(foundFlights.stream()
                .map(Flight::getId)
                .collect(Collectors.toList()))
                .containsExactly(flight1, flight2);

    }

    private long createFlight
            (String flightNumber, long takeoff, long landing, LocalDateTime dateTime) {
        return airportService.createFlight(
                flightNumber,
                takeoff,
                landing,
                dateTime).getId();

    }


}
