package com.pokedemo.exeption.mapper;

import com.pokedemo.exeption.PokemonNotFoundException;
import com.pokedemo.models.dto.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class PokemonNotFoundExceptionMapper
        implements ExceptionMapper<PokemonNotFoundException> {

    private static final Logger LOG = Logger.getLogger(PokemonNotFoundExceptionMapper.class);

    @Override
    public Response toResponse(PokemonNotFoundException e) {
        LOG.errorf(e, "Pokémon non trovato: %s", e.getPokemonName());

        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(404, e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}