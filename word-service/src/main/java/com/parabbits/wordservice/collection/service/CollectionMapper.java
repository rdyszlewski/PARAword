package com.parabbits.wordservice.collection.service;

import com.parabbits.wordservice.collection.data.Language;
import com.parabbits.wordservice.collection.data.WordsCollection;

public class CollectionMapper {

    public static WordsCollection toObject(CollectionDTO collectionDTO) {
        WordsCollection wordsCollection = new WordsCollection();
        wordsCollection.setId(collectionDTO.getId());
        wordsCollection.setName(collectionDTO.getName());
        wordsCollection.setDescription(collectionDTO.getDescription());
        wordsCollection.setLearningLanguage(new Language(collectionDTO.getLanguage1()));
        wordsCollection.setNativeLanguage(new Language(collectionDTO.getLanguage2()));
        wordsCollection.setIsPublic(collectionDTO.isPublic());
        wordsCollection.setUser(collectionDTO.getUserId());

        return wordsCollection;
    }

    public static CollectionDTO toDTO(WordsCollection collection) {
        return new CollectionDTO(collection.getId(), collection.getName(), collection.getDescription(),
                collection.getLearningLanguage().getId(), collection.getNativeLanguage().getId(),
                collection.getUser(), collection.getIsPublic());
    }
}
