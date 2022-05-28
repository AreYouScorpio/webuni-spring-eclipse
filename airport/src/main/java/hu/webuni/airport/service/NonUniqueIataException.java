package hu.webuni.airport.service;

public class NonUniqueIataException extends RuntimeException{

    public NonUniqueIataException(String iata) {
        super("Existing IATA: "+ iata);
    }
}
