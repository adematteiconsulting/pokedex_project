package service;

import com.pokedemo.Enums.TranslationLanguage;
import com.pokedemo.client.FunTranslationsClient;
import com.pokedemo.models.funtranslation.request.FunTranslationsRequest;
import com.pokedemo.models.funtranslation.response.FunTranslationTranslateResponse;
import com.pokedemo.models.funtranslation.response.FunTranslationsContentsResponse;
import com.pokedemo.service.FunTranslationsServiceImpl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class FunTranslationsServiceImplTest {

    @Inject
    FunTranslationsServiceImpl funTranslationsService;

    @InjectMock
    @RestClient
    FunTranslationsClient funTranslationsClient;

    @Test
    void getTranslation_shouldReturnYodaTranslation() {
        FunTranslationTranslateResponse mockResponse = new FunTranslationTranslateResponse(
                new FunTranslationsContentsResponse("Underground it lives, yes."));
        Mockito.when(funTranslationsClient.translateYoda(any(FunTranslationsRequest.class)))
                .thenReturn(mockResponse);

        String result = funTranslationsService.getTranslation("It lives underground.", TranslationLanguage.YODA);

        assertEquals("Underground it lives, yes.", result);
        Mockito.verify(funTranslationsClient).translateYoda(any());
        Mockito.verify(funTranslationsClient, Mockito.never()).translateShakespeare(any());
    }

    @Test
    void getTranslation_shouldReturnShakespeareTranslation() {
        FunTranslationTranslateResponse mockResponse = new FunTranslationTranslateResponse(
                new FunTranslationsContentsResponse("Hark, 't doth live beneath the earth."));
        Mockito.when(funTranslationsClient.translateShakespeare(any(FunTranslationsRequest.class)))
                .thenReturn(mockResponse);

        String result = funTranslationsService.getTranslation("It lives underground.", TranslationLanguage.SHAKESPEARE);

        assertEquals("Hark, 't doth live beneath the earth.", result);
        Mockito.verify(funTranslationsClient).translateShakespeare(any());
        Mockito.verify(funTranslationsClient, Mockito.never()).translateYoda(any());
    }

    @Test
    void getTranslation_shouldReturnNullWhenClientThrows() {
        Mockito.when(funTranslationsClient.translateYoda(any()))
                .thenThrow(new RuntimeException("Service unavailable"));

        String result = funTranslationsService.getTranslation("It lives underground.", TranslationLanguage.YODA);

        assertNull(result);
    }
}