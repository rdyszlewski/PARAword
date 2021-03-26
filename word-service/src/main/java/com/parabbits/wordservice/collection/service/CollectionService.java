package com.parabbits.wordservice.collection.service;


import com.parabbits.wordservice.collection.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CollectionService {

    // TODO: poprawić pobieranie iwelkości kolekcji
    // TODO: pomyśleć, w jaki sposób dodać nazwy użytkownikóœ

    private LanguageRepository languageRepository;
    private CollectionRepository collectionRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
//        this.languageRepository = languageRepository;
    }

//    public List<WordsCollection> getByUser(Long userId) {
//        return collectionRepository.findByUser(userId);
//    }

    public CollectionResponseDTO getById(Long id) {
        Optional<WordsCollection> optionalCollection = collectionRepository.findById(id);
        if (optionalCollection.isPresent()) {
            WordsCollection collection = optionalCollection.get();
            return createResponseDTO(collection);
        }
        return null;
    }

    private CollectionResponseDTO createResponseDTO(WordsCollection collection) {
        long collectionSize = collectionRepository.countWordsById(collection.getId());
        // TODO: jak rozwiązać ten problem
        return new CollectionResponseDTO(collection.getId(), collection.getName(), collection.getDescription(),
                collection.getLearningLanguage().getAbbreviation(), collection.getNativeLanguage().getAbbreviation(),
                String.valueOf(collection.getUser()),
                collectionSize);
    }

    public List<CollectionResponseDTO> getByUser(Long userId) {
        List<WordsCollection> collections = collectionRepository.findByUser(userId);
        return collections.stream().map(this::createResponseDTO).collect(Collectors.toList());
    }

    public List<CollectionResponseDTO> getByFilter(CollectionFilter filter) {
        List<WordsCollection> collections = collectionRepository.findAll(CollectionSpecification.getSpecification(filter));
        return collections.stream().map(this::createResponseDTO).collect(Collectors.toList());
    }

    public CollectionResponseDTO addCollection(CollectionDTO collectionDTO) {
        if (validate(collectionDTO)) {
            WordsCollection collection = createWordsCollection(collectionDTO);
            WordsCollection savedCollection = collectionRepository.save(collection);
            return createResponseDTO(savedCollection);
        }
        return null;
    }

    private boolean validate(CollectionDTO collectionDTO) {
        if (collectionDTO.getName() == null || collectionDTO.getName().strip().isEmpty()
                || collectionDTO.getLanguage1() <= 0 || collectionDTO.getLanguage2() <= 0
                || collectionDTO.getUserId() <= 0) {
            // TODO: dodać odpowiedni komunikat
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (collectionRepository.existsById(collectionDTO.getId())) {
            throw new IllegalArgumentException("Id is already used");
        }
        return true;
    }

    private WordsCollection createWordsCollection(CollectionDTO collectionDTO) {
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

    // TODO: dodawanie kolekcji
    // TODO: usuwanie kolekcji
    // TODO: edycja kolekcji


}
