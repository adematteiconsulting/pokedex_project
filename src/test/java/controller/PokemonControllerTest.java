package controller;

import com.pokedemo.exeption.PokemonNotFoundException;
import com.pokedemo.models.dto.PokemonServiceResponse;
import com.pokedemo.service.PokemonServiceImpl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class PokemonControllerTest {

    @InjectMock
    PokemonServiceImpl pokemonService;

    @Test
    void getPokemonInfo_shouldReturn200WithPokemonData() {
        Mockito.when(pokemonService.getPokemonInfo("diglett"))
                .thenReturn(new PokemonServiceResponse("diglett", "cave", false,
                        "Lives underground."));

        given()
                .when().get("/pokemon/diglett")
                .then()
                .statusCode(200)
                .body("name", equalTo("diglett"))
                .body("habitat", equalTo("cave"))
                .body("isLegendary", equalTo(false))
                .body("flavorTextEntries", equalTo("Lives underground."));
    }

    @Test
    void getPokemonInfo_shouldReturn404WhenPokemonNotFound() {
        Mockito.when(pokemonService.getPokemonInfo("unknown"))
                .thenThrow(new PokemonNotFoundException("unknown"));

        given()
                .when().get("/pokemon/unknown")
                .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", containsString("unknown"));
    }

    @Test
    void getTranslatedPokemonInfo_shouldReturn200WithTranslatedDescription() {
        Mockito.when(pokemonService.getTranslatedPokemonInfo("diglett"))
                .thenReturn(new PokemonServiceResponse("diglett", "cave", false,
                        "Underground it lives, yes."));

        given()
                .when().get("/pokemon/translated/diglett")
                .then()
                .statusCode(200)
                .body("name", equalTo("diglett"))
                .body("flavorTextEntries", equalTo("Underground it lives, yes."));
    }

    @Test
    void getTranslatedPokemonInfo_shouldReturn404WhenPokemonNotFound() {
        Mockito.when(pokemonService.getTranslatedPokemonInfo("unknown"))
                .thenThrow(new PokemonNotFoundException("unknown"));

        given()
                .when().get("/pokemon/translated/unknown")
                .then()
                .statusCode(404)
                .body("status", equalTo(404));
    }
}