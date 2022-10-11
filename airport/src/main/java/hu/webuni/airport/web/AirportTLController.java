package hu.webuni.airport.web;

import hu.webuni.airport.dto.AddressDto;
import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.dto.FlightDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AirportTLController {

    int a; // teszt, kiírjuk hello a=0, hogy frissül-e

    private List<AirportDto> allAirports = new ArrayList<>();
    {
//  temporarly swithed off
//  mert tesztelgetjuk haladon az entity graph-okat es a konstruktor bovul
//  allAirports.add( new AirportDto(1, "Ferenc Liszt Airport", "BUD", new AddressDto(1, "Nyul", "Iskola", "9082")));

    }





    @GetMapping("/")
    public String home() {
        System.out.println("hello"); // teszt kiírás frissül-e
        System.out.println(a); // teszt kiírás frissül-e
        return "index";
    }

    @GetMapping("/airports")
    public String listAirports(Map<String, Object> model){
        model.put("airports", allAirports);
        model.put("newAirport", new AirportDto());
        return "airports";

    }

    @PostMapping("/airports")
    public String addAirport(AirportDto airport){
        allAirports.add(airport);
        return "redirect:airports";

    }



}
