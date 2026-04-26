package com.pokedemo.mapper;

import com.pokedemo.models.dto.PokemonServiceResponse;
import com.pokedemo.models.pokeapi.response.PokemonApiResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PokemonSpeciesMapper {
    public static PokemonServiceResponse toServiceResponse(PokemonApiResponse species) {
        PokemonServiceResponse response = new PokemonServiceResponse();

        response.setName(species.getName());

        response.setHabitat(
                species.getHabitat() != null ? species.getHabitat().getName() : "Unknown"
        );

        response.setIsLegendary(species.isLegendary());

        response.setFlavorTextEntries(
                species.getFlavorTextEntries().stream()
                        .filter(f -> f.language.name.equals("en"))
                        .findFirst()
                        .map(f -> f.getFlavorText().replace("\n", " ").replace("\f", " "))
                        .orElse("Description unavailable"));

        return response;
    }
}
