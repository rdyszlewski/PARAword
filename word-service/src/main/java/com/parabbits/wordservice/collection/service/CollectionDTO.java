package com.parabbits.wordservice.collection.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CollectionDTO {

    private Long id;
    private String name;
    private String description;
    private long language1;
    private long language2;
    private long userId;
    private boolean isPublic;
}
