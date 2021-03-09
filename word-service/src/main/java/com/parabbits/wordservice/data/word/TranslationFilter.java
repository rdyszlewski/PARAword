package com.parabbits.wordservice.data.word;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "innerBuilder")
public class TranslationFilter {

    private long userId;
    private String name;
    private long collectionId;
    private PartOfSpeech partOfSpeech;
    private int meaning;

    // TODO: zastanowić się, czy do tłumaczeń potrzebne będzie id użytkownika
    public static TranslationFilter.TranslationFilterBuilder builder(long userId){
        assert userId> 0;
        return innerBuilder().userId(userId);
    }

}
