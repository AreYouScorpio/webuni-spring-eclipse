package hu.webuni.airport.mapper;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.model.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


    @Mapper(componentModel = "spring")
    public interface AirportMapper {

        List<AirportDto> airportsToDtos(List<Airport> airports);

        AirportDto airportToDto(Airport airport);

        Airport dtoToAirport(AirportDto airportDto);
    }

        /*

        https://mapstruct.org/ ----> example:

        CarMapper INSTANCE = Mappers.getMapper( CarMapper.class ); 3

        @Mapping(source = "numberOfSeats", target = "seatCount")
        CarDto carToCarDto(Car car); 2

         */

