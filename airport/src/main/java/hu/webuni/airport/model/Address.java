package hu.webuni.airport.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Address {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long id;

    private String city;
    private String street;
    private String zip;

    public Address(long id, String city, String street, String zip) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.zip = zip;
    }

    public Address() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
