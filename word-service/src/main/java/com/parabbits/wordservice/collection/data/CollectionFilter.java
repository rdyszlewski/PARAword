package com.parabbits.wordservice.collection.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class CollectionFilter {

    private String name;
    private String description;
    private Long language1;
    private Long language2;
    private Long userId;
    private Boolean publicCollection;

}
