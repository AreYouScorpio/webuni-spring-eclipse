package hu.webuni.airport.service;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.model.Airport;
import hu.webuni.airport.repository.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Service
public class AirportService {

    // Spring Data injection
    AirportRepository airportRepository;

    // Spring Data injection, @Autowired would be also okay but now constructor injection generate:

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
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
    public Airport save(Airport airport){
        checkUniqueIata(airport.getIata(), null);
        // EntityManager miatt ehelyett
        // airports.put(airport.getId(), airport);
        // ez lesz:
        // em.persist(airport);
        // return airport; ---> Spring data--->
        return airportRepository.save(airport);
    }

    @Transactional
    public Airport update(Airport airport){
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

            if(count>0)
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
}
