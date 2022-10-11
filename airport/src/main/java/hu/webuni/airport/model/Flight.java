package hu.webuni.airport.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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



}
