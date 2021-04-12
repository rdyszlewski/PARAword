package com.parabbits.wordservice.data.word;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "innerBuilder")
public class TranslationFilter {

    @Builder.Default
    private final long userId = -1;
    private final String name;
    private final String word;
    private final String description;
    @Builder.Default
    private final long collectionId = -1;
    private final PartOfSpeech partOfSpeech;
    @Builder.Default
    private final int meaning = -1;
    @Builder.Default
    private final long languageId = -1;

    public static TranslationFilter.TranslationFilterBuilder builder(long userId) {
        assert userId > 0;
        return innerBuilder().userId(userId);
    }

}
