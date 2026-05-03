package com.pokedemo.models.funtranslation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunTranslationTranslateResponse {
    @JsonProperty("contents")
    private FunTranslationsContentsResponse contents;
}
