package hu.webuni.airport.dto;

public class AirportDto {
    private long id;
    private String name;
    private String iata;

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


    public AirportDto(long id, String name, String iata) {
        super();
        this.id = id;
        this.name = name;
        this.iata = iata;
    }

    public AirportDto() {
    }

}
