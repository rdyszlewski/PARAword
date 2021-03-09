package com.parabbits.wordservice.data.word;

import com.parabbits.wordservice.security.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.servlet.http.Part;

@Getter
@Builder(builderMethodName = "innerBuilder")
public class WordFilter {

    @NotNull
    private long userId;
    private String name;
    private long collectionId;
    private PartOfSpeech partOfSpeech;
    // TODO: dodać tutaj kategorię
    private int limit;

    public static WordFilterBuilder builder(long userId){
        assert userId> 0;
        return innerBuilder().userId(userId);
    }

}
