package com.parabbits.wordservice.collection.service;


import com.parabbits.wordservice.collection.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CollectionService {

    private LanguageRepository languageRepository;
    private CollectionRepository collectionRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public CollectionResponseDTO getById(Long id) {
        Optional<WordsCollection> optionalCollection = collectionRepository.findById(id);
        if (optionalCollection.isPresent()) {
            WordsCollection collection = optionalCollection.get();
            return CollectionResponseMapper.toDTO(collection, collectionRepository);
        }
        return null;
    }

    public CollectionAccess getCollectionAccess(long collectionId) {
        Optional<CollectionAccess> optionalResult = collectionRepository.findCollectionAccess(collectionId);
        return optionalResult.orElse(new CollectionAccess(null, null));
    }


    public List<CollectionResponseDTO> getByUser(Long userId) {
        List<WordsCollection> collections = collectionRepository.findByUser(userId);
        return collections.stream().map(x -> CollectionResponseMapper.toDTO(x, collectionRepository)).collect(Collectors.toList());
    }

    public List<CollectionResponseDTO> getByFilter(CollectionFilter filter) {
        List<WordsCollection> collections = collectionRepository.findAll(CollectionSpecification.getSpecification(filter));
        return collections.stream().map(x -> CollectionResponseMapper.toDTO(x, collectionRepository)).collect(Collectors.toList());
    }

    public CollectionResponseDTO addCollection(CollectionDTO collectionDTO) {
        if (validate(collectionDTO, true)) {
            WordsCollection collection = CollectionMapper.toObject(collectionDTO);
            WordsCollection savedCollection = collectionRepository.save(collection);
            return CollectionResponseMapper.toDTO(savedCollection, collectionRepository);
        }
        return null;
    }

    private boolean validate(CollectionDTO collectionDTO, boolean checkId) {
        if (collectionDTO.getName() == null || collectionDTO.getName().strip().isEmpty()
                || collectionDTO.getLanguage1() <= 0 || collectionDTO.getLanguage2() <= 0
                || collectionDTO.getUserId() <= 0) {
            // TODO: dodać odpowiedni komunikat
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (checkId && collectionRepository.existsById(collectionDTO.getId())) {
            throw new IllegalArgumentException("Id is already used");
        }
        return true;
    }


    public void updateCollection(CollectionDTO collectionDTO) {
        if (validate(collectionDTO, false)) {
            WordsCollection collection = CollectionMapper.toObject(collectionDTO);
            collectionRepository.save(collection);
        }
    }

    public void removeCollection(Long id) {
        collectionRepository.deleteById(id);
    }
}
