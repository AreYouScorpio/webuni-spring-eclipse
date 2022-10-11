package hu.webuni.airport.web;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.mapper.AirportMapper;
import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.LogEntry;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.LogEntryRepository;
import hu.webuni.airport.service.AirportService;
import hu.webuni.airport.service.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    //https://mapstruct.org/ minták !!! és pom.xml --- https://mapstruct.org/documentation/installation/

    @Autowired
    AirportService airportService;

    @Autowired
    AirportMapper airportMapper;

    @Autowired
    AirportRepository airportRepository;

    //@Autowired
    //LogEntryService logEntryService;

    @GetMapping
    public List<AirportDto> getAll() {
        // eredetileg ennyi: return airportMapper.airportsToDtos(airportService.findAll());
        // de most kíváncsiak vagyunk, mi toltodik be mar automatikusan DB-bol
        //List<Airport> airports = airportService.findAll(); //--> ehelyett meg a repobol mar az uj lekerest hivjuk meg es nem a service-bol, igy gyorsabb, ugyis csak athivna
        List<Airport> airports = airportRepository.findAllWithAddress();
        // es mi akkor kenyszerul betoltodni, amikor a mapstruct mar a gettereket hivogatja:
        return airportMapper.airportsToDtos(airports);


    }


    @GetMapping("/{id}")
    public AirportDto getById(@PathVariable long id) {
        Airport airport = airportService.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));

        // deleted after mapper ---> AirportDto airportDto = airports.get(id);
//        if (airportDto!=null)
//            return ResponseEntity.ok(airportDto);
//        else
//        return ResponseEntity.notFound().build();
       /* ehelyett is orElseThrow és a return marad
        if (airport != null)
            return airportMapper.airportToDto(airport);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        */
        return airportMapper.airportToDto(airport);

    }


    @PostMapping
    public AirportDto createAirport(@RequestBody @Valid AirportDto airportDto /*, BindingResult errors */) {
        //if (errors.hasErrors()) throw new ...


        // áthelyezve mapper bevezetésével a service-be:
        // checkUniqueIata(airportDto.getIata());

        Airport airport = airportService.save(airportMapper.dtoToAirport(airportDto));
        // szintén törölve áthelyezés miatt --> airports.put(airportDto.getId(), airportDto);
        // return airportDto; --->
        return airportMapper.airportToDto(airport);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable long id) {
        airportService.delete(id);
    }


    /*

    @PutMapping("/{id}")
    public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id,
                                                    @RequestBody AirportDto airportDto) {
        if (!airports.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }

        checkUniqueIata(airportDto.getIata());
        airportDto.setId(id);
        airports.put(id, airportDto);
        return ResponseEntity.ok(airportDto);
    }
new PutMapping after MapStruct added:
---->

    */

    /* saját mego:

    @PutMapping("/{id}")
    public AirportDto modifyAirport(@PathVariable long id,
                                                    @RequestBody @Valid AirportDto airportDto) {

        Airport airport = airportService.findById(id);


        if (airport != null)
            airportService.update(id, airportMapper.dtoToAirport(airportDto));
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);


        return airportMapper.airportToDto(airport);


    }



     */


    // ---> ehelyett tanári megoldás, de enyém is működött ---->


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')") //ez csak akkor értékelődik ki, ha a SecurityConfigban megkérem @EnableGlobalMethodSecurityben
    public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id,
                                                    @RequestBody AirportDto airportDto) {

        Airport airport = airportMapper.dtoToAirport(airportDto);
        airport.setId(id); // hogy tudjunk módosítani azonos iata-jút a uniqecheck ellenére
        try {
            AirportDto savedAirportDto = airportMapper.airportToDto(airportService.update(airport));

            // LogEntryRepository.save(new LogEntry("Airport modified with id " + id)); -- service hozzáadva
            // logEntryService.createLog("Airport modified with id " + id); -inkább a service update legyen felelős érte, h a logot lementse
            // a service autowired-et is lehet így innét törölni, átvinni AirportService-be


            return ResponseEntity.ok(savedAirportDto);
        }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }






}
