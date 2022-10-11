
package hu.webuni.airport.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Airport {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include()
    private long id;

    @Size(min = 3, max = 20)
    private String name;
    private String iata;

    @ManyToOne
    private Address address;

    public Airport(String name, String iata) {
        this.name = name;
        this.iata = iata;
    }



}



/* airport old, QueryDsl-lel működött, most váltok tanári lombokos verzióra, próbaképp - 10.10.2022

package hu.webuni.airport.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
// SpringData .. namedquieries dropped:
//@NamedQuery(name = "Airport.countByIata",
//        query = "SELECT COUNT(a.id) from Airport a WHERE a.iata = : iata ")
//@NamedQuery(name = "Airport.countByIataAndIdNotIn",
//        query = "SELECT COUNT(a.id) from Airport a WHERE a.iata = : iata AND a.id!= : id")
public class Airport {

    @Id
    @GeneratedValue // (strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include()
    private long id;

    @Size (min = 3, max = 20)
    private String name;
    //@Size (min = 3, max = 10)
    private String iata;

    @ManyToOne
    private Address address;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }


    public Airport(long id, String name, String iata) {
        super();
        this.id = id;
        this.name = name;
        this.iata = iata;
    }

    public Airport(long id, String name, String iata, Address address) {
        this.id = id;
        this.name = name;
        this.iata = iata;
        this.address = address;
    }

    public Airport(String name, String iata) {
        this.name = name;
        this.iata = iata;
    }


    public Airport() {
    }


}

 */


