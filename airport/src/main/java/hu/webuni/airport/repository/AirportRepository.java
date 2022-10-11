package hu.webuni.airport.repository;

import hu.webuni.airport.model.Airport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Long countByIata(String iata);
    Long countByIataAndIdNot(String iata, Long id);

    //v1: @Query("SELECT a FROM Airport a LEFT JOIN FETCH a.address") // az addresseket is szeretnem betolteni, euert LEFT JOIN..
    //v2: nem querybe irom, hanem entitas grafba, akk ez a ket sor kell a v1 helyett:
    @EntityGraph(attributePaths = {"address"}) //kapcsos kell, m tombot fogad el, most egyelemu tombunk van
    @Query("SELECT a FROM Airport a")
    List<Airport> findAllWithAddress(); // es mst akkor a controllerben ne a findAll-t hivjuk a service-n at, hanem a repositorybol ezt a metodust !

}
