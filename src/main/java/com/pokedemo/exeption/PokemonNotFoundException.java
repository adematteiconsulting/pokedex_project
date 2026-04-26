package com.pokedemo.exeption;

public class PokemonNotFoundException extends RuntimeException {

    private final String pokemonName;

    public PokemonNotFoundException(String pokemonName) {
        super("Pokémon '" + pokemonName + "' non trovato");
        this.pokemonName = pokemonName;
    }

    public String getPokemonName() {
        return pokemonName;
    }

}