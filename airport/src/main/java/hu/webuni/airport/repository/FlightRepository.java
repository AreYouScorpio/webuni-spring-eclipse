package hu.webuni.airport.repository;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.model.QFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Optional;

import static hu.webuni.airport.model.Flight_.takeoffTime;

public interface FlightRepository extends
        JpaRepository<Flight, Long>,
        JpaSpecificationExecutor<Flight>,
        QuerydslPredicateExecutor<Flight>,
        QuerydslBinderCustomizer<QFlight> {

    @Override
    default void customize(QuerydslBindings bindings, QFlight flight) {
        bindings.bind(flight.flightNumber).first((path, value) -> path.startsWithIgnoreCase(value));
        bindings.bind(flight.takeoff.iata).first((path, value) -> path.startsWith(value));

        // this is without from to date:
//        bindings.bind(flight.takeoffTime).first((path, value) ->
//
//        {
//            LocalDateTime startOfDay =
//                    LocalDateTime.of(value.toLocalDate(), LocalTime.MIDNIGHT);
//            return path.between(startOfDay, startOfDay.plusDays(1));
//        });

        // this is from to date version:

        bindings.bind(flight.takeoffTime).all((path, values) ->

        { if (values.size() !=2) return Optional.empty();

            Iterator<? extends LocalDateTime> iterator = values.iterator();
            LocalDateTime start = iterator.next();
            LocalDateTime end = iterator.next();
            LocalDateTime startOfDay =
                    LocalDateTime.of(start.toLocalDate(), LocalTime.MIDNIGHT);
            LocalDateTime endOfDay =
                    LocalDateTime.of(end.toLocalDate(), LocalTime.MIDNIGHT).plusDays(1);
            return Optional.ofNullable(path.between(startOfDay, endOfDay));
        });

    }


    //QueryDSL 2. course-hoz ezt is hozzá kell adni, h Predicate is átadható legyen, az AirportService-ben (korábbi FlightSpecben )

}