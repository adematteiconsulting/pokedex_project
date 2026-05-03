package service;

import com.pokedemo.Enums.TranslationLanguage;
import com.pokedemo.client.PokeApiClient;
import com.pokedemo.exeption.PokemonNotFoundException;
import com.pokedemo.models.dto.PokemonServiceResponse;
import com.pokedemo.models.pokeapi.response.PokemonApiResponse;
import com.pokedemo.models.pokeapi.response.PokemonFlavorTextEntryResponse;
import com.pokedemo.models.pokeapi.response.PokemonHabitatResponse;
import com.pokedemo.service.FunTranslationsServiceImpl;
import com.pokedemo.service.PokemonServiceImpl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
class PokemonServiceImplTest {

    @Inject
    PokemonServiceImpl pokemonService;

    @InjectMock
    @RestClient
    PokeApiClient pokeApiClient;

    @InjectMock
    FunTranslationsServiceImpl funTranslationsService;

    // -------------------------------------------------------------------------
    // getPokemonInfo
    // -------------------------------------------------------------------------

    @Test
    void getPokemonInfo_shouldReturnMappedResponse() {
        Mockito.when(pokeApiClient.getPokemonSpecies("diglett"))
                .thenReturn(buildApiResponse("diglett", "cave", false,
                        List.of(entry("en", "Lives underground."))));

        PokemonServiceResponse result = pokemonService.getPokemonInfo("diglett");

        assertEquals("diglett", result.getName());
        assertEquals("cave", result.getHabitat());
        assertFalse(result.getIsLegendary());
        assertEquals("Lives underground.", result.getFlavorTextEntries());
    }

    @Test
    void getPokemonInfo_shouldThrowPokemonNotFoundExceptionOn404() {
        Response notFound = Response.status(404).build();
        Mockito.when(pokeApiClient.getPokemonSpecies("unknown"))
                .thenThrow(new WebApplicationException(notFound));

        assertThrows(PokemonNotFoundException.class,
                () -> pokemonService.getPokemonInfo("unknown"));
    }

    @Test
    void getPokemonInfo_shouldThrowRuntimeExceptionOnOtherErrors() {
        Response serverError = Response.status(500).build();
        Mockito.when(pokeApiClient.getPokemonSpecies("diglett"))
                .thenThrow(new WebApplicationException(serverError));

        assertThrows(RuntimeException.class,
                () -> pokemonService.getPokemonInfo("diglett"));
    }

    // -------------------------------------------------------------------------
    // getTranslatedPokemonInfo — habitat cave → YODA
    // -------------------------------------------------------------------------

    @Test
    void getTranslatedPokemonInfo_shouldUseYodaTranslationWhenHabitatIsCave() {
        Mockito.when(pokeApiClient.getPokemonSpecies("diglett"))
                .thenReturn(buildApiResponse("diglett", "cave", false,
                        List.of(entry("en", "Lives underground."))));
        Mockito.when(funTranslationsService.getTranslation("Lives underground.", TranslationLanguage.YODA))
                .thenReturn("Underground it lives, yes.");

        PokemonServiceResponse result = pokemonService.getTranslatedPokemonInfo("diglett");

        assertEquals("Underground it lives, yes.", result.getFlavorTextEntries());
        Mockito.verify(funTranslationsService).getTranslation(any(), eq(TranslationLanguage.YODA));
    }

    // -------------------------------------------------------------------------
    // getTranslatedPokemonInfo — habitat != cave → SHAKESPEARE
    // -------------------------------------------------------------------------

    @Test
    void getTranslatedPokemonInfo_shouldUseShakespeareTranslationWhenHabitatIsNotCave() {
        Mockito.when(pokeApiClient.getPokemonSpecies("bulbasaur"))
                .thenReturn(buildApiResponse("bulbasaur", "grassland", false,
                        List.of(entry("en", "A Pokémon that grows a plant."))));
        Mockito.when(funTranslationsService.getTranslation(
                        "A Pokémon that grows a plant.", TranslationLanguage.SHAKESPEARE))
                .thenReturn("Hark, a Pokémon doth grow a plant.");

        PokemonServiceResponse result = pokemonService.getTranslatedPokemonInfo("bulbasaur");

        assertEquals("Hark, a Pokémon doth grow a plant.", result.getFlavorTextEntries());
        Mockito.verify(funTranslationsService).getTranslation(any(), eq(TranslationLanguage.SHAKESPEARE));
    }

    @Test
    void getTranslatedPokemonInfo_shouldKeepOriginalDescriptionWhenTranslationFails() {
        Mockito.when(pokeApiClient.getPokemonSpecies("diglett"))
                .thenReturn(buildApiResponse("diglett", "cave", false,
                        List.of(entry("en", "Lives underground."))));
        Mockito.when(funTranslationsService.getTranslation(any(), any()))
                .thenThrow(new RuntimeException("Translation service down"));

        PokemonServiceResponse result = pokemonService.getTranslatedPokemonInfo("diglett");

        assertEquals("Lives underground.", result.getFlavorTextEntries());
    }

    @Test
    void getTranslatedPokemonInfo_shouldMapLegendaryCorrectly() {
        Mockito.when(pokeApiClient.getPokemonSpecies("mewtwo"))
                .thenReturn(buildApiResponse("mewtwo", "rare", true,
                        List.of(entry("en", "A legendary Pokémon."))));
        Mockito.when(funTranslationsService.getTranslation(any(), any()))
                .thenReturn("A legendary Pokémon, 't is.");

        PokemonServiceResponse result = pokemonService.getTranslatedPokemonInfo("mewtwo");

        assertTrue(result.getIsLegendary());
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private PokemonApiResponse buildApiResponse(String name, String habitatName,
                                                boolean isLegendary,
                                                List<PokemonFlavorTextEntryResponse> entries) {
        return new PokemonApiResponse(name, new PokemonHabitatResponse(habitatName),
                isLegendary, entries);
    }

    private PokemonFlavorTextEntryResponse entry(String languageName, String text) {
        PokemonFlavorTextEntryResponse e = new PokemonFlavorTextEntryResponse();
        e.setFlavorText(text);
        PokemonFlavorTextEntryResponse.Language lang = new PokemonFlavorTextEntryResponse.Language();
        lang.name = languageName;
        e.language = lang;
        return e;
    }
}