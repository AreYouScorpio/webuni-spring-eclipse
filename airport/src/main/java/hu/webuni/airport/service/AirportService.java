package hu.webuni.airport.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.model.QFlight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.Access;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AirportService {

    // Spring Data injection
    AirportRepository airportRepository;
    FlightRepository flightRepository;
    LogEntryService logEntryService; // ide hoztuk át a controllerből,de nem autowired, hanem új constructorral a háromra

    public AirportService(AirportRepository airportRepository, FlightRepository flightRepository, LogEntryService logEntryService) {
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
        this.logEntryService = logEntryService;
    }
// Spring Data injection, @Autowired would be also okay but now constructor injection generate:

    //már 3 van, új konstruktor:
    //public AirportService(AirportRepository airportRepository, FlightRepository flightRepository) {
    //    this.airportRepository = airportRepository;
    //    this.flightRepository = flightRepository;
    //}


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
        if (airportRepository.existsById(airport.getId())) {
            //return airport;
            //return em.merge(airport); SD--->

            logEntryService.createLog(String.format("Airport modified with id %d new name is %s",
                    airport.getId(),
                    airport.getName()));

            callBackendSystem(); // mesterséges hiba generátor
            return airportRepository.save(airport);
        } else
            throw new NoSuchElementException();
    }

    private void callBackendSystem() {
        if (new Random().nextInt(4) == 1) throw new RuntimeException();
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

    /* masodik tanfolyamban atirjuk QueryDSL-re:

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

        if (takeoffTime != null) // SpringFramework-ös StringUtils
            spec = spec.and(FlightSpecifications.hasTakeoffTime(takeoffTime));


        return flightRepository.findAll(spec, Sort.by("id"));
    }


     */


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

    @Transactional
    public List<Airport> findaAllWithRelationships() {
        List<Airport> airports = airportRepository.findAllWithAddressAndDepartures();
        airports = airportRepository.findAllWithArrivals();
        return airports; // igy nem 100x100 jon be, igaz nem egy, hanem 2 queryvel, de igy csak 100+100 jon vissza
    }
}
