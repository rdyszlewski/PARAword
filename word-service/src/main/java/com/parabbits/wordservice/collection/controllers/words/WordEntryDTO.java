package com.parabbits.wordservice.collection.controllers.words;

import java.util.List;
import java.util.Objects;

public class WordEntryDTO {

    private WordDTO word;
    private List<TranslationDTO> translations;

    public WordEntryDTO(WordDTO word, List<TranslationDTO> translations) {
        this.word = word;
        this.translations = translations;
    }

    public WordDTO getWord() {
        return word;
    }

    public void setWord(WordDTO word) {
        this.word = word;
    }

    public List<TranslationDTO> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationDTO> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordEntryDTO that = (WordEntryDTO) o;
        return word.equals(that.word) &&
                translations.equals(that.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, translations);
    }
}
