package com.pokedemo.service;

import com.pokedemo.Enums.TranslationLanguage;
import com.pokedemo.client.FunTranslationsClient;
import com.pokedemo.models.funtranslation.request.FunTranslationsRequest;
import com.pokedemo.models.funtranslation.response.FunTranslationTranslateResponse;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
@ApplicationScoped
@Slf4j
public class FunTranslationsServiceImpl {

    @RestClient
    private FunTranslationsClient funTranslationsClient;

    public String getTranslation(String text, TranslationLanguage language) {
        log.info("starting translation of thext: {} ,into {} language from funtranslations", text, language);
        FunTranslationsRequest request = new FunTranslationsRequest(text);
        try {
            FunTranslationTranslateResponse response;
            if(language.equals(TranslationLanguage.YODA)){
                response= funTranslationsClient.translateYoda(request);
            }else{
                response= funTranslationsClient.translateShakespeare(request);
            }
            log.info("successfully translated into {} language from funtranslations ", language);
            return response.getContents().getTranslated();
        } catch (Exception e) {
            log.error("error while translating text into {} language ", language, e);
            return null;
        }
    }
}
