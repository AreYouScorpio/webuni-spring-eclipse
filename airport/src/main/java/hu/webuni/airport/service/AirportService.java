package hu.webuni.airport.service;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.model.Airport;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirportService {

    private Map<Long, Airport> airports = new HashMap<>();

    {
        airports.put(1L, new Airport(1,"abc", "XYZ"));
        airports.put(2L, new Airport(2,"def", "UVW"));
    }

    public Airport save(Airport airport){
        checkUniqueIata(airport.getIata());
        airports.put(airport.getId(), airport);
        return airport;
    }

    private void checkUniqueIata(String iata) {
        Optional<Airport> airportWithSameIata = airports.values()
                .stream()
                .filter(a -> a.getIata().equals(iata))
                .findAny();
        if (airportWithSameIata.isPresent())
            throw new NonUniqueIataException(iata);
    }

    public List<Airport> findAll() {
        return new ArrayList<>(airports.values());
    }
    public Airport findById(long id) {
        return airports.get(id);
    }

    public void delete(long id) {
        airports.remove(id);
    }
}
