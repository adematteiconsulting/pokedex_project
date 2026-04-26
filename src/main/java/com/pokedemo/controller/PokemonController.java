package com.pokedemo.controller;

import com.pokedemo.models.dto.PokemonServiceResponse;
import com.pokedemo.service.PokemonService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pokemon")
@Produces(MediaType.APPLICATION_JSON)
public class PokemonController {
    @Inject
    PokemonService pokemonService;
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemonInfo(@PathParam("name") String name) {
        PokemonServiceResponse response = pokemonService.getPokemonInfo(name);
        return Response.ok(response).build();
    }
}
