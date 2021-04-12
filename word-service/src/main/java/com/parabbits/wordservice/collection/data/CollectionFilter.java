package com.parabbits.wordservice.collection.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class CollectionFilter {

    private final String name;
    private final String description;
    private final Long language1;
    private final Long language2;
    private final Long userId;
    private final Boolean publicCollection;

}
