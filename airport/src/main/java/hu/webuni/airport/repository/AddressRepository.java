package hu.webuni.airport.repository;

import hu.webuni.airport.model.Address;
import hu.webuni.airport.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
