package com.parabbits.wordservice.collection.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectionFilter {

    private String name;
    private String description;
    private long language1;
    private long language2;
    private long userId;
    private boolean publicCollection;

}
