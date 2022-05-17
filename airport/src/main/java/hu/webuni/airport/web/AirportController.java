package hu.webuni.airport.web;

import hu.webuni.airport.dto.AirportDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private Map<Long, AirportDto> airports = new HashMap<>();

    {
        airports.put(1L, new AirportDto(1,"abc", "XYZ"));
        airports.put(2L, new AirportDto(2,"def", "UVW"));
    }

    @GetMapping
    public List<AirportDto> getAll(){
        return new ArrayList<>(airports.values());
    }

}
