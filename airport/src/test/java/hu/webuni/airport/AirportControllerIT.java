package hu.webuni.airport;

import hu.webuni.airport.dto.AirportDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // server indítás random porton
public class AirportControllerIT {

    private static final String BASE_URI="/api/airports";

    @Autowired
    WebTestClient webTestClient; // tudja, melyik porton fut a server a localhoston, és oda küldi a kéréseket majd a teszt idejére

    @Test
    void testThatCreatedAirportIsListed() throws Exception {
        List<AirportDto> airportsBefore = getAllAirports();
        AirportDto newAirport = new AirportDto(5, "jdlksj" , "IGH");
        createAirport(newAirport);

        List<AirportDto>  airportsAfter = getAllAirports();

        assertThat(airportsAfter.subList(0, airportsBefore.size()))
                .containsExactlyElementsOf(airportsBefore);

        assertThat(airportsAfter.get(airportsAfter.size()-1)).isEqualTo(newAirport);

    }

    private void createAirport(AirportDto newAirport) {
        webTestClient
                .post()
                .uri(BASE_URI)
                .bodyValue(newAirport)
                .exchange()
                .expectStatus()
                .isOk();
    }

    private List<AirportDto> getAllAirports() {
        List<AirportDto> responseList = webTestClient
                .get()
                .uri(BASE_URI)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AirportDto.class)
                .returnResult().getResponseBody();

        Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;
    }
}
