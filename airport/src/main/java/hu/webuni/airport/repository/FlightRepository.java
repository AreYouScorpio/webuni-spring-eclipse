package hu.webuni.airport.repository;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FlightRepository extends
        JpaRepository<Flight, Long>,
        JpaSpecificationExecutor<Flight>,
        QuerydslPredicateExecutor<Flight> {


    //QueryDSL 2. course-hoz ezt is hozzá kell adni, h Predicate is átadható legyen, az AirportService-ben (korábbi FlightSpecben )

}
