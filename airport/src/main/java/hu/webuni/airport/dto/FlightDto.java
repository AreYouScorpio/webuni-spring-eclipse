package hu.webuni.airport.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightDto {

    private long id;

    @NotEmpty
    private String flightNumber;

    @NotNull
    private LocalDateTime takeoffTime;

    private AirportDto takeoff;

    private AirportDto landing;




}
