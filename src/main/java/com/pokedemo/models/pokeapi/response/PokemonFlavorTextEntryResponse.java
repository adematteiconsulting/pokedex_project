package com.pokedemo.models.pokeapi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonFlavorTextEntryResponse {
    @JsonProperty("flavor_text")
    private String flavorText;

    public Language language;

    public static class Language {
        public String name;
    }

}
