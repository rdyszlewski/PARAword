package com.parabbits.wordservice.controllers.words;

import com.parabbits.wordservice.data.word.PartOfSpeech;

import java.util.List;
import java.util.Objects;

public class WordDTO {

    private long id;
    private String word;
    private String description;
    private PartOfSpeech partOfSpeech;
    private long collectionId;

    public WordDTO(long id, String word) {
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

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
    }

    // TODO: kolekcja prawdodpodbnie tutaj nie będzie potrzebna
    // TODO: tutaj można ustawić tłumaczenia


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordDTO wordDTO = (WordDTO) o;
        return id == wordDTO.id &&
                collectionId == wordDTO.collectionId &&
                word.equals(wordDTO.word) &&
                Objects.equals(description, wordDTO.description) &&
                partOfSpeech == wordDTO.partOfSpeech;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, description, partOfSpeech, collectionId);
    }
}
