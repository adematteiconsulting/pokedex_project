package com.pokedemo.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PokemonServiceResponse {
    private String name;
    private String habitat;
    private Boolean isLegendary;
    private String flavorTextEntries;
}
