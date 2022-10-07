package hu.webuni.airport.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Flight {


    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long id;


    private String flightNumber;
    private LocalDateTime takeoffTime;

    @ManyToOne
    private Airport takeoff;

    @ManyToOne
    private Airport landing;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Airport getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(Airport takeoff) {
        this.takeoff = takeoff;
    }

    public Airport getLanding() {
        return landing;
    }

    public void setLanding(Airport landing) {
        this.landing = landing;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDateTime getTakeoffTime() {
        return takeoffTime;
    }

    public void setTakeoffTime(LocalDateTime takeoffTime) {
        this.takeoffTime = takeoffTime;
    }


    public Flight(long id, String flightNumber, LocalDateTime takeoffTime, Airport takeoff, Airport landing) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.takeoffTime = takeoffTime;
        this.takeoff = takeoff;
        this.landing = landing;
    }

    public Flight() {
    }
}
