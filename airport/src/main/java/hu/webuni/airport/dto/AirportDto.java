package hu.webuni.airport.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder // ha van Builder, akk nem kell külön constructor, ha csak egyes attributumokat akarok es jol is lathato
@AllArgsConstructor
@NoArgsConstructor
public class AirportDto {

    private long id;
    @Size (min = 3, max = 20)
    private String name;
    private String iata;

    private AddressDto address;


}
