package com.parabbits.wordservice.data.word;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "innerBuilder")
public class TranslationFilter {

    private final long userId = -1;
    private final String name;
    private final String word;
    private final String description;
    private final long collectionId = -1;
    private final PartOfSpeech partOfSpeech;
    @Builder.Default
    private final int meaning = -1;
    private final long languageId = -1;

    public static TranslationFilter.TranslationFilterBuilder builder(long userId) {
        assert userId > 0;
        return innerBuilder().userId(userId);
    }

}
