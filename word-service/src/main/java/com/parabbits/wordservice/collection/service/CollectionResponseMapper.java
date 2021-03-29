package com.parabbits.wordservice.collection.service;

import com.parabbits.wordservice.collection.data.CollectionRepository;
import com.parabbits.wordservice.collection.data.Language;
import com.parabbits.wordservice.collection.data.WordsCollection;

public class CollectionResponseMapper {

    public static WordsCollection toObject(CollectionResponseDTO collectionDTO) {
        WordsCollection wordsCollection = new WordsCollection();
        wordsCollection.setId(collectionDTO.getId());
        wordsCollection.setName(collectionDTO.getName());
        wordsCollection.setDescription(collectionDTO.getDescription());
        Language language1 = new Language(collectionDTO.getLanguage1().getId());
        Language language2 = new Language(collectionDTO.getLanguage2().getId());
        wordsCollection.setLearningLanguage(language1);
        wordsCollection.setNativeLanguage(language2);
        wordsCollection.setIsPublic(collectionDTO.getIsPublic());
        // TODo: przemyśleć to jakoś inaczej
        Long userId = Long.parseLong(collectionDTO.getUserName());
        wordsCollection.setUser(userId);

        return wordsCollection;
    }

    public static CollectionResponseDTO toDTO(WordsCollection collection, CollectionRepository repository) {
        long collectionSize = repository.countWordsById(collection.getId());
        // TODO: tutaj można zrobić jakiś cache tych języków
        LanguageDTO language1 = new LanguageDTO(collection.getLearningLanguage().getId(), collection.getLearningLanguage().getAbbreviation());
        LanguageDTO language2 = new LanguageDTO(collection.getNativeLanguage().getId(), collection.getNativeLanguage().getAbbreviation());
        return new CollectionResponseDTO(collection.getId(), collection.getName(), collection.getDescription(),
                language1, language2,
                String.valueOf(collection.getUser()),
                collectionSize, collection.getIsPublic());
    }
}
