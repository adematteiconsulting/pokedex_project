package com.pokedemo.service;

import com.pokedemo.client.PokeApiClient;
import com.pokedemo.exeption.PokemonNotFoundException;
import com.pokedemo.mapper.PokemonSpeciesMapper;
import com.pokedemo.models.dto.PokemonServiceResponse;
import com.pokedemo.models.pokeapi.response.PokemonApiResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class PokemonService {

    @RestClient
    private PokeApiClient pokeApiClient;


    public PokemonServiceResponse getPokemonInfo(String name) {
        try {
            PokemonApiResponse species = pokeApiClient.getPokemonSpecies(name);
            return PokemonSpeciesMapper.toServiceResponse(species);
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                throw new PokemonNotFoundException(name);  // <- qui hai il nome corretto
            }
            throw new RuntimeException("Errore durante la chiamata alla PokeAPI");
        }
    }
}
