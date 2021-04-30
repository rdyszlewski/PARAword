package com.parabbits.wordservice.collection.controllers.words;

import java.util.List;
import java.util.Objects;

public class SimpleWordEntryDTO {

    private long id;
    private String word;
    private List<String> translations;

    public SimpleWordEntryDTO(long id, String word, List<String> translations) {
        this.id = id;
        this.word = word;
        this.translations = translations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public void setTranslations(List<String> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleWordEntryDTO that = (SimpleWordEntryDTO) o;
        return id == that.id &&
                word.equals(that.word) &&
                translations.equals(that.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, translations);
    }
}
