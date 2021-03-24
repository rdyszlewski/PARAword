package com.parabbits.wordservice.data.collection;


import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CollectionService {

    private LanguageRepository languageRepository;
    private CollectionRepository collectionRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository, LanguageRepository languageRepository) {
        this.collectionRepository = collectionRepository;
        this.languageRepository = languageRepository;
    }

    public List<WordsCollection> getByUser(Long userId) {
        return collectionRepository.findByUser(userId);
    }

    public Optional<WordsCollection> getById(Long id) {
        return collectionRepository.findById(id);
    }

    public Optional<WordsCollection> getDetailsById(Long id) {
        throw new NotYetImplementedException();
    }
    

    
}
