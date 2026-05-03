package com.pokedemo.Enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum TranslationLanguage {
    YODA("yoda"),
    SHAKESPEARE("shakespeare");

    private final String value;

    TranslationLanguage(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static TranslationLanguage fromValue(String value) {
        for (TranslationLanguage language : TranslationLanguage.values()) {
            if (language.value.equalsIgnoreCase(value)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Value not valid for TranslationLanguage: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
