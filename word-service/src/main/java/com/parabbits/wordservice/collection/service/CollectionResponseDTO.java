package com.parabbits.wordservice.collection.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CollectionResponseDTO {

    private long id;
    private String name;
    private String description;
    private String language1;
    private String language2;
    private String userName;
    private long wordsCount;


}
