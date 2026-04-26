package com.pokedemo.exeption.mapper;

import com.pokedemo.exeption.PokemonNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

@Provider
public class PokeApiExceptionMapper
        implements ResponseExceptionMapper<RuntimeException> {

    @Override
    public RuntimeException toThrowable(Response response) {
        return switch (response.getStatus()) {
            case 500 -> new RuntimeException("PokeAPI non disponibile");
            case 404 -> null;
            default  -> new RuntimeException("Errore: " + response.getStatus());
        };
    }
}