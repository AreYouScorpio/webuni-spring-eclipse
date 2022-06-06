package hu.webuni.airport.repository;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {


}
