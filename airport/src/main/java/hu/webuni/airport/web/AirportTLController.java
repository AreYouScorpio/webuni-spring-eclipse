package hu.webuni.airport.web;

import hu.webuni.airport.dto.AirportDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AirportTLController {

    int a;

    private List<AirportDto> allAirports = new ArrayList<>();
    {
        allAirports.add( new AirportDto(1, "Ferenc Liszt Airport", "BUD"));

    }





    @GetMapping("/")
    public String home() {
        System.out.println("hello");
        System.out.println(a);
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
