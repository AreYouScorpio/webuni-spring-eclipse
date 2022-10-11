package hu.webuni.airport.repository;

import hu.webuni.airport.model.Airport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Long countByIata(String iata);
    Long countByIataAndIdNot(String iata, Long id);


    /* EAGER with SUBSELECT version was it:

    //v1: @Query("SELECT a FROM Airport a LEFT JOIN FETCH a.address") // az addresseket is szeretnem betolteni, euert LEFT JOIN..
    //v2: nem querybe irom, hanem entitas grafba, akk ez a ket sor kell a v1 helyett:
    @EntityGraph(attributePaths = {"address"}, type = EntityGraph.EntityGraphType.LOAD) //kapcsos kell, m tombot fogad el, most egyelemu tombunk van
    @Query("SELECT a FROM Airport a")
    List<Airport> findAllWithAddress(); // es mst akkor a controllerben ne a findAll-t hivjuk a service-n at, hanem a repositorybol ezt a metodust !

    //fetch graph - szigoruan csak a megnevezett toltodik be, meg akk sem a tobbi, ha eager az entiti graph, ezertkell atirni load graph-ra
    //type = EntityGraph.EntityGraphType.LOAD .. igy a default eager-ek is benne lesznek, definialas nelkul is

     */

    //es most jon a Prod kornyezetben legszerencsesebb megoldas:
    //igy csak egy SELECT fut le, semmi felesleges JOIN nincs benne full false-nal, full-nal pedig hozza van minden joinolva, ami kell

    /*hogy ne jojjon vissza N*M adat, ezt is atirjuk:
    @EntityGraph(attributePaths = {"address", "departures", "arrivals"}) //, type = EntityGraph.EntityGraphType.LOAD) //kapcsos kell, m tombot fogad el, most egyelemu tombunk van .. fetchelje be az arrivalst is
    @Query("SELECT a FROM Airport a")
    List<Airport> findAllWithAddressAndDepartures();
     */

    @EntityGraph(attributePaths = {"address", "departures"}) // itt egy departure-hoz egy address van, nincs Descartes szorzat//, "arrivals"}) //, type = EntityGraph.EntityGraphType.LOAD) //kapcsos kell, m tombot fogad el, most egyelemu tombunk van .. fetchelje be az arrivalst is
    @Query("SELECT a FROM Airport a")
    List<Airport> findAllWithAddressAndDepartures(Pageable pageable);

    @EntityGraph(attributePaths = {"arrivals"})
    @Query("SELECT a FROM Airport a")
    List<Airport> findAllWithArrivals(Pageable pageable);

}
