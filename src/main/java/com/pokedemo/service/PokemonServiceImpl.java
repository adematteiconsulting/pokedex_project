package com.pokedemo.service;

import com.pokedemo.Enums.Habitat;
import com.pokedemo.Enums.TranslationLanguage;
import com.pokedemo.client.PokeApiClient;
import com.pokedemo.exeption.PokemonNotFoundException;
import com.pokedemo.mapper.PokemonSpeciesMapper;
import com.pokedemo.models.dto.PokemonServiceResponse;
import com.pokedemo.models.pokeapi.response.PokemonApiResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
@Slf4j
@ApplicationScoped
public class PokemonServiceImpl {

    @RestClient
    private PokeApiClient pokeApiClient;

    @Inject
    private FunTranslationsServiceImpl funTranslationsService;


    public PokemonServiceResponse getPokemonInfo(String name) {
        log.info("Starting get pokemon info for {}", name);
        try {
            PokemonApiResponse species = pokeApiClient.getPokemonSpecies(name);
            return PokemonSpeciesMapper.toServiceResponse(species);
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                throw new PokemonNotFoundException(name);  // <- qui hai il nome corretto
            }
            throw new RuntimeException("Error calling PokeApi");
        }
    }

    public PokemonServiceResponse getTranslatedPokemonInfo(String name){
        log.info("Starting getTranslatedPokemonInfo for {}", name);
        PokemonServiceResponse speciesResponse = getPokemonInfo(name);
        String description = speciesResponse.getFlavorTextEntries();
        try {
            if(speciesResponse.getHabitat().equals(Habitat.CAVE.getValue())|| speciesResponse.getIsLegendary()) {
                description = funTranslationsService.getTranslation(description, TranslationLanguage.YODA);
            }else{
                description = funTranslationsService.getTranslation(description, TranslationLanguage.SHAKESPEARE);
            }
        }catch (Exception e){
            log.error("error translating flavour text for {}", name, e);
        }
        speciesResponse.setFlavorTextEntries(description);
        return speciesResponse;
    }
}
