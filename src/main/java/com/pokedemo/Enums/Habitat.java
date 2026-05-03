package com.pokedemo.Enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Habitat {
    CAVE("cave");

    private final String value;

    Habitat(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
