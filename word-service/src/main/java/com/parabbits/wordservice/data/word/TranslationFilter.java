package com.parabbits.wordservice.data.word;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "innerBuilder")
public class TranslationFilter {

    private long userId = -1;
    private String name;
    private String word;
    private String description;
    private long collectionId = -1;
    private PartOfSpeech partOfSpeech;
    @Builder.Default
    private int meaning = -1;
    private long languageId = -1;

    public static TranslationFilter.TranslationFilterBuilder builder(long userId){
        assert userId> 0;
        return innerBuilder().userId(userId);
    }

}
