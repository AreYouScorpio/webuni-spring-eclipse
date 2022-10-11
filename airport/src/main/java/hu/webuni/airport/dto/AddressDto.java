package hu.webuni.airport.dto;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
public class AddressDto {

    private long id;

    private String city;
    private String street;
    private String zip;

    public AddressDto(long id, String city, String street, String zip) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.zip = zip;
    }


}
