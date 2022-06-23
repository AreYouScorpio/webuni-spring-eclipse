package hu.webuni.airport.service;

import hu.webuni.airport.model.Flight;
import hu.webuni.airport.model.Flight_;
import org.springframework.data.jpa.domain.Specification;

public class FlightSpecifications {

public static Specification<Flight> hasId(long id) {
    return (root, cq, cb) -> cb.equal(root.get(Flight_.id), id);
}

    {

}



}
