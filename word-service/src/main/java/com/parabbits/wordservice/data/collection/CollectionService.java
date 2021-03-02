package com.parabbits.wordservice.data.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionService {

    private LanguageRepository languageRepository;
    private CollectionRepository collectionRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository, LanguageRepository languageRepository){
        this.collectionRepository = collectionRepository;
        this.languageRepository = languageRepository;
    }

    // TODO: tworzenie nowego zestawu
    // TODO: pobieranie zestawu z wszystkimi słówkami

    public List<WordsCollection> getAllCollections(Long userId){
        List<WordsCollection> collections = collectionRepository.findByUser(userId);
        return collections;
    }

    public Optional<WordsCollection> getById(Long id){
        // TODO: możliwe, że tutaj będziemy chcieli również pobrać wszystkie słowa. Ale nie wiadomo, czy z tłumaczeniami czy też nie
        return collectionRepository.findById(id);
    }

    public Optional<Language> getLanguageByAbbreviation(String abbreviation){
        return languageRepository.findByAbbreviation(abbreviation);
    }

    public void addCollection(WordsCollection collection){
        collectionRepository.save(collection);
    }

}
