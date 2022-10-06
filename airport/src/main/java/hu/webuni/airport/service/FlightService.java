package hu.webuni.airport.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FlightService {

    @Autowired
    AirportRepository airportRepository;
    @Autowired
    FlightRepository flightRepository;

    @Transactional
    public Flight save(Flight flight) {
        //a takeoff/landing airportból csak az id-t vesszük figyelembe, már létezniük kell
        flight.setTakeoff(airportRepository.findById(flight.getTakeoff().getId()).get());
        flight.setLanding(airportRepository.findById(flight.getLanding().getId()).get());
        return flightRepository.save(flight);
    }

    public List<Flight> findFlightsByExample(Flight example) {

        long id = example.getId();
        String flightNumber = example.getFlightNumber();
        String takeoffIata = null;
        Airport takeoff = example.getTakeoff();
        if (takeoff != null)
            takeoffIata = takeoff.getIata();
        LocalDateTime takeoffTime = example.getTakeoffTime();

        Specification<Flight> spec = Specification.where(null);

        if (id > 0) {
            spec = spec.and(FlightSpecifications.hasId(id));
        }

        if (StringUtils.hasText(flightNumber))
            spec = spec.and(FlightSpecifications.hasFlightNumber(flightNumber));

        if (StringUtils.hasText(takeoffIata))
            spec = spec.and(FlightSpecifications.hasTakeoffIata(takeoffIata));

        if (takeoffTime != null)
            spec = spec.and(FlightSpecifications.hasTakeoffTime(takeoffTime));

        return flightRepository.findAll(spec, Sort.by("id"));
    }

}
