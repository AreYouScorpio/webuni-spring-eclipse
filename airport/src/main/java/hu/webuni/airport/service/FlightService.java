package hu.webuni.airport.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import hu.webuni.airport.model.QFlight;
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

        //Specification<Flight> spec = Specification.where(null); // üres Specification, ami semmire nem szűr

        // a FlightSpecifications feltételeit ide írjuk bele közvetlen --->

        ArrayList<Predicate> predicates = new ArrayList<com.querydsl.core.types.Predicate>();

        QFlight flight = QFlight.flight;

        if (id > 0) {

            // spec = spec.and(FlightSpecifications.hasId(id));
            predicates.add(flight.id.eq(id));
        }

        if (StringUtils.hasText(flightNumber)) // SpringFramework-ös StringUtils
            // spec = spec.and(FlightSpecifications.hasFlightNumber(flightNumber));
            predicates.add(flight.flightNumber.startsWithIgnoreCase(flightNumber));

        if (StringUtils.hasText(takeoffIata)) // SpringFramework-ös StringUtils
            //spec = spec.and(FlightSpecifications.hasTakeoffIata(takeoffIata));
            predicates.add(flight.takeoff.iata.startsWith(takeoffIata));

        if (takeoffTime != null) // SpringFramework-ös StringUtils
        //spec = spec.and(FlightSpecifications.hasTakeoffTime(takeoffTime));
        {
            LocalDateTime startOfDay =
                    LocalDateTime.of(takeoffTime.toLocalDate(),
                            LocalTime.MIDNIGHT);
            predicates.add(flight.takeoffTime.between(startOfDay, startOfDay.plusDays(1)));

        }


        //return flightRepository.findAll(spec, Sort.by("id"));
        return Lists.newArrayList(flightRepository.findAll(ExpressionUtils.allOf(predicates)));
    }

}
