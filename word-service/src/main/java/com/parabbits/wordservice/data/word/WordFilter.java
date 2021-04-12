package com.parabbits.wordservice.data.word;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "innerBuilder")
public class WordFilter {

    @NotNull
    private final long userId;
    private final String name;
    private final long collectionId;
    private final PartOfSpeech partOfSpeech;
    // TODO: dodać tutaj kategorię
    // TODO: prawdopodobnie można doddać też język
    private final int limit;

    public static WordFilterBuilder builder(long userId) {
        assert userId > 0;
        return innerBuilder().userId(userId);
    }

}
