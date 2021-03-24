package com.parabbits.wordservice.data.word;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "innerBuilder")
public class WordFilter {

    @NotNull
    private long userId;
    private String name;
    private long collectionId;
    private PartOfSpeech partOfSpeech;
    // TODO: dodać tutaj kategorię
    // TODO: prawdopodobnie można doddać też język
    private int limit;

    public static WordFilterBuilder builder(long userId){
        assert userId> 0;
        return innerBuilder().userId(userId);
    }

}
