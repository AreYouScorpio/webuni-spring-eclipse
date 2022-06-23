package hu.webuni.airport.service;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AirportService {

    // Spring Data injection
    AirportRepository airportRepository;
    FlightRepository flightRepository;

    // Spring Data injection, @Autowired would be also okay but now constructor injection generate:

    public AirportService(AirportRepository airportRepository, FlightRepository flightRepository) {
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
    }


    // Spring Data miatt meg ez is törölve lett ->

    //@PersistenceContext
    //EntityManager em;

    /* EntityManager miatt törölve:

    private Map<Long, Airport> airports = new HashMap<>();

    {
        airports.put(1L, new Airport(1,"abc", "XYZ"));
        airports.put(2L, new Airport(2,"def", "UVW"));
    }


     */

    @Transactional
    public Airport save(Airport airport) {
        checkUniqueIata(airport.getIata(), null);
        // EntityManager miatt ehelyett
        // airports.put(airport.getId(), airport);
        // ez lesz:
        // em.persist(airport);
        // return airport; ---> Spring data--->
        return airportRepository.save(airport);
    }

    @Transactional
    public Airport update(Airport airport) {
        // airports.put(id, airport);
        checkUniqueIata(airport.getIata(), airport.getId());
        if (airportRepository.existsById(airport.getId()))
            //return airport;
            //return em.merge(airport); SD--->
            return airportRepository.save(airport);
        else throw new NoSuchElementException();
    }

    private void checkUniqueIata(String iata, Long id) {

        boolean forUpdate = id != null;

        // TypedQuery<Long> query = em.createNamedQuery( forUpdate ?
        //                "Airport.countByIataAndIdNotIn" :
        //                "Airport.countByIata", Long.class)
        //        .setParameter("iata", iata);
        // if (forUpdate) query.setParameter("id", id);
        // Long count = query
        //        .getSingleResult(); // a ", Long.class" helyett jó az is, ha (Long)-ra castoljuk
        /*
        Optional<Airport> airportWithSameIata = airports.values()
                .stream()
                .filter(a -> a.getIata().equals(iata))
                .findAny();
        */
        //if (airportWithSameIata.isPresent())

        //---> new -->
        Long count = forUpdate ?
                airportRepository.countByIataAndIdNot(iata, id)
                : airportRepository.countByIata(iata);

        if (count > 0)
            throw new NonUniqueIataException(iata);
    }

    public List<Airport> findAll() {
        // return new ArrayList<>(airports.values());
        // return em.createQuery("SELECT a from Airport a", Airport.class).getResultList();

        return airportRepository.findAll();
    }

    public Optional<Airport> findById(long id) {
        //return airports.get(id);
        //return em.find(Airport.class, id);
        return airportRepository.findById(id);
    }

    @Transactional
    public void delete(long id) {
        // em.remove(findById(id));
        // airports.remove(id);
        airportRepository.deleteById(id);
    }

    @Transactional
    public Flight createFlight(String flightNumber, long takeoffAirportId, long landingAirportId, LocalDateTime takeoffDateTime) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setTakeoff(airportRepository.findById(takeoffAirportId).get());
        flight.setLanding(airportRepository.findById(landingAirportId).get());
        flight.setTakeoffTime(takeoffDateTime);
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

        Specification<Flight> spec = Specification.where(null); // üres Specification, ami semmire nem szűr

        if (id > 0) {
            spec = spec.and(FlightSpecifications.hasId(id));
        }

        if (StringUtils.hasText(flightNumber)) // SpringFramework-ös StringUtils
            spec = spec.and(FlightSpecifications.hasFlightNumber(flightNumber));


        if (StringUtils.hasText(takeoffIata)) // SpringFramework-ös StringUtils
            spec = spec.and(FlightSpecifications.hasTakeoffIata(takeoffIata));

        if (takeoffTime!=null) // SpringFramework-ös StringUtils
            spec = spec.and(FlightSpecifications.hasTakeoffTime(takeoffTime));


        return flightRepository.findAll(spec, Sort.by("id"));
    }

}
