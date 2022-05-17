package hu.webuni.airport.web;

import hu.webuni.airport.dto.AirportDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<AirportDto> getById(@PathVariable long id){
        AirportDto airportDto = airports.get(id);
        if (airportDto!=null)
            return ResponseEntity.ok(airportDto);
        else
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public AirportDto createAirport(@RequestBody AirportDto airportDto) {
        airports.put(airportDto.getId(), airportDto);
        return airportDto;
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id,
                                    @RequestBody AirportDto airportDto) {
        if(!airports.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }

        airportDto.setId(id);
        airports.put(id, airportDto);
        return ResponseEntity.ok(airportDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAirport(@PathVariable long id){
        airports.remove(id);
    }

}
