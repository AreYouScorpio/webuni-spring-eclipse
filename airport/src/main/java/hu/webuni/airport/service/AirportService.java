package hu.webuni.airport.service;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.model.Airport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Service
public class AirportService {

    @PersistenceContext
    EntityManager em;

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
        em.persist(airport);
        return airport;
    }

    @Transactional
    public Airport update(Airport airport){
        // airports.put(id, airport);
        checkUniqueIata(airport.getIata(), airport.getId());
        //return airport;
        return em.merge(airport);
    }

    private void checkUniqueIata(String iata, Long id) {

        boolean forUpdate = (id != null);
        TypedQuery<Long> query = em.createNamedQuery( forUpdate ?
                        "Airport.countByIataAndIdNotIn" :
                        "Airport.countByIata", Long.class)
                .setParameter("iata", iata);
        if (forUpdate) query.setParameter("id", id);
        Long count = query
                .getSingleResult(); // a ", Long.class" helyett jó az is, ha (Long)-ra castoljuk
        /*
        Optional<Airport> airportWithSameIata = airports.values()
                .stream()
                .filter(a -> a.getIata().equals(iata))
                .findAny();
        */
        //if (airportWithSameIata.isPresent())
            if(count>0)
            throw new NonUniqueIataException(iata);
    }

    public List<Airport> findAll() {
        // return new ArrayList<>(airports.values());
        return em.createQuery("SELECT a from Airport a", Airport.class).getResultList();
    }

    public Airport findById(long id) {
        //return airports.get(id);
        return em.find(Airport.class, id);
    }

    @Transactional
    public void delete(long id) {
        em.remove(findById(id));
        //airports.remove(id);
    }
}
