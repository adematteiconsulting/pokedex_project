package com.pokedemo.models.pokeapi.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonApiResponse {
    @JsonProperty("name")
    private String name;
    @JsonProperty("habitat")
    private PokemonHabitatResponse habitat;
    @JsonProperty("is_legendary")
    private boolean isLegendary;
    @JsonProperty("flavor_text_entries")
    private List<PokemonFlavorTextEntryResponse> flavorTextEntries;

}
