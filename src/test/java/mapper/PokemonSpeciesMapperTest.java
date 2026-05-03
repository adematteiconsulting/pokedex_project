package mapper;

import com.pokedemo.mapper.PokemonSpeciesMapper;
import com.pokedemo.models.dto.PokemonServiceResponse;
import com.pokedemo.models.pokeapi.response.PokemonApiResponse;
import com.pokedemo.models.pokeapi.response.PokemonFlavorTextEntryResponse;
import com.pokedemo.models.pokeapi.response.PokemonHabitatResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonSpeciesMapperTest {

    @Test
    void toServiceResponse_mapNameHabitatAndLegendary() {
        PokemonApiResponse api = buildResponse("diglett", "cave", false,
                List.of(entry("en", "Lives underground.")));

        PokemonServiceResponse result = PokemonSpeciesMapper.toServiceResponse(api);

        assertEquals("diglett", result.getName());
        assertEquals("cave", result.getHabitat());
        assertFalse(result.getIsLegendary());
    }

    @Test
    void toServiceResponse_pickFirstEngFlavorText() {
        PokemonApiResponse api = buildResponse("diglett", "cave", false, List.of(
                entry("ja", "日本語テキスト"),
                entry("en", "Lives about one\nyard underground\nwhere it feeds on\fplant roots."),
                entry("en", "A second english entry.")));

        PokemonServiceResponse result = PokemonSpeciesMapper.toServiceResponse(api);

        assertEquals("Lives about one yard underground where it feeds on plant roots.",
                result.getFlavorTextEntries());
    }

    @Test
    void toServiceResponse_cleanNewlinesAndFormFeeds() {
        PokemonApiResponse api = buildResponse("bulbasaur", "grassland", false,
                List.of(entry("en", "Line one\nline two\fline three")));

        PokemonServiceResponse result = PokemonSpeciesMapper.toServiceResponse(api);

        assertEquals("Line one line two line three", result.getFlavorTextEntries());
    }

    @Test
    void toServiceResponse_shouldCollapseMultipleSpaces() {
        PokemonApiResponse api = buildResponse("bulbasaur", "grassland", false,
                List.of(entry("en", "Word\n\fextra")));

        PokemonServiceResponse result = PokemonSpeciesMapper.toServiceResponse(api);

        assertEquals("Word extra", result.getFlavorTextEntries());
    }

    @Test
    void toServiceResponse_shouldReturnDescriptionUnavailableWhenNoEnglishEntry() {
        PokemonApiResponse api = buildResponse("diglett", "cave", false,
                List.of(entry("ja", "日本語のみ")));

        PokemonServiceResponse result = PokemonSpeciesMapper.toServiceResponse(api);

        assertEquals("Description unavailable", result.getFlavorTextEntries());
    }

    @Test
    void toServiceResponse_returnUnknownHabitatIsNull() {
        PokemonApiResponse api = buildResponse("missingno", null, false,
                List.of(entry("en", "A mysterious Pokémon.")));

        PokemonServiceResponse result = PokemonSpeciesMapper.toServiceResponse(api);

        assertEquals("Unknown", result.getHabitat());
    }

    @Test
    void toServiceResponse_mapIsLegendaryTrue() {
        PokemonApiResponse api = buildResponse("mewtwo", "rare", true,
                List.of(entry("en", "A legendary Pokémon.")));

        PokemonServiceResponse result = PokemonSpeciesMapper.toServiceResponse(api);

        assertTrue(result.getIsLegendary());
    }

    private PokemonApiResponse buildResponse(String name, String habitatName,
                                             boolean isLegendary,
                                             List<PokemonFlavorTextEntryResponse> entries) {
        PokemonHabitatResponse habitat = habitatName != null
                ? new PokemonHabitatResponse(habitatName)
                : null;
        return new PokemonApiResponse(name, habitat, isLegendary, entries);
    }

    private PokemonFlavorTextEntryResponse entry(String languageName, String text) {
        PokemonFlavorTextEntryResponse entry = new PokemonFlavorTextEntryResponse();
        entry.setFlavorText(text);
        PokemonFlavorTextEntryResponse.Language lang = new PokemonFlavorTextEntryResponse.Language();
        lang.name = languageName;
        entry.language = lang;
        return entry;
    }
}