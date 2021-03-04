package com.parabbits.wordservice.controllers.words;

import com.parabbits.wordservice.data.word.PartOfSpeech;

import java.util.Objects;

public class TranslationDTO {

    private long id;
    private String word;
    private String description;
    private int meaning;
    private PartOfSpeech partOfSpeech;

    // TODO: zastanowić się, czy coś tutaj jest jeszcze potrzebne


    public TranslationDTO(long id, String word) {
        this.id = id;
        this.word = word;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMeaning() {
        return meaning;
    }

    public void setMeaning(int meaning) {
        this.meaning = meaning;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationDTO that = (TranslationDTO) o;
        return id == that.id &&
                meaning == that.meaning &&
                word.equals(that.word) &&
                Objects.equals(description, that.description) &&
                partOfSpeech == that.partOfSpeech;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, description, meaning, partOfSpeech);
    }
}
