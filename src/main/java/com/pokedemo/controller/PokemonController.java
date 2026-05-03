package com.pokedemo.controller;

import com.pokedemo.models.dto.PokemonServiceResponse;
import com.pokedemo.service.PokemonServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/pokemon")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
public class PokemonController {
    @Inject
    PokemonServiceImpl pokemonService;
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemonInfo(@PathParam("name") String name) {
        log.info("Called getPokemonInfo for {}", name);
        PokemonServiceResponse response = pokemonService.getPokemonInfo(name);
        return Response.ok(response).build();
    }

    @GET
    @Path("translated/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTranslatedPokemonInfo(@PathParam("name") String name) {
        log.info("Called getTranslatedPokemonInfo for {}", name);
        PokemonServiceResponse response = pokemonService.getTranslatedPokemonInfo(name);
        return Response.ok(response).build();
    }
}
