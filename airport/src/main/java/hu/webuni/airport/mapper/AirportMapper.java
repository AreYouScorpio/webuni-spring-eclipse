package hu.webuni.airport.mapper;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.dto.FlightDto;
import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;


    @Mapper(componentModel = "spring")
    public interface AirportMapper {

        List<AirportDto> airportsToDtos(List<Airport> airports);

        @IterableMapping(qualifiedByName = "summary") //hasznalja a summary-s parjat,metodust
        List<AirportDto> airportSummariesToDtos(List<Airport> airports);

        AirportDto airportToDto(Airport airport);

        @Named("summary")
        @Mapping(target = "address", ignore = true) // address lazy esetben ha address-t probalna kitolteni, ignore = true legyen.. ez csak a summary mapper
        @Mapping(target = "departures", ignore = true)
        AirportDto airportSummaryToDto(Airport airport);

        Airport dtoToAirport(AirportDto airportDto);

        @Mapping(target = "takeoff", ignore = true) // nem szeretnem h a takeoff benne legyen, ez a vegtelen ciklusos korok megelozese mapstruct szinten
        @Mapping(target = "landing", ignore = true)
        FlightDto flightToDto(Flight flight);
    }

        /*

        https://mapstruct.org/ ----> example:

        CarMapper INSTANCE = Mappers.getMapper( CarMapper.class ); 3

        @Mapping(source = "numberOfSeats", target = "seatCount")
        CarDto carToCarDto(Car car); 2

         */

