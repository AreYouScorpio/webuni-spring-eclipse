package hu.webuni.airport.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.airport.dto.FlightDto;
import hu.webuni.airport.model.Flight;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    Flight dtoToFlight(FlightDto flightDto);

    FlightDto flightToDto(Flight flight);
    List<FlightDto> flightsToDtos(List<Flight> flight);

    List<FlightDto> flightsToDtos(Iterable<Flight> findAll);

}