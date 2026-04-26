package com.pokedemo.client;

import com.pokedemo.exeption.mapper.PokeApiExceptionMapper;
import com.pokedemo.models.pokeapi.response.PokemonApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/v2")
@RegisterRestClient(baseUri = "https://pokeapi.co")
@RegisterProvider(PokeApiExceptionMapper.class)
public interface PokeApiClient {
    @GET
    @Path("/pokemon-species/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    PokemonApiResponse getPokemonSpecies(@PathParam("name") String name);
}
