package com.pokedemo.client;

import com.pokedemo.exeption.mapper.PokeApiExceptionMapper;
import com.pokedemo.models.funtranslation.request.FunTranslationsRequest;
import com.pokedemo.models.funtranslation.response.FunTranslationTranslateResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/v1/translate")
@RegisterRestClient(baseUri = "https://api.funtranslations.mercxry.me")
@RegisterProvider(PokeApiExceptionMapper.class)
public interface FunTranslationsClient {
    @POST
    @Path("/shakespeare")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    FunTranslationTranslateResponse translateShakespeare(@RequestBody FunTranslationsRequest request);
    @POST
    @Path("/yoda")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    FunTranslationTranslateResponse translateYoda(@RequestBody FunTranslationsRequest request);
}
