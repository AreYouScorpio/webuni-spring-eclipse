package hu.webuni.airport.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirportDto {

    private long id;
    @Size (min = 3, max = 20)
    private String name;
    private String iata;

    private AddressDto address;


}
