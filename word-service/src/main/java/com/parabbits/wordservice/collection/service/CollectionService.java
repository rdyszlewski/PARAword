package com.parabbits.wordservice.collection.service;


import com.parabbits.wordservice.collection.data.CollectionRepository;
import com.parabbits.wordservice.collection.data.LanguageRepository;
import com.parabbits.wordservice.collection.data.WordsCollection;
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
    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
//        this.languageRepository = languageRepository;
    }

    public List<WordsCollection> getByUser(Long userId) {
        return collectionRepository.findByUser(userId);
    }

    public CollectionResponseDTO getById(Long id) {
        Optional<WordsCollection> optionalCollection = collectionRepository.findById(id);
        // TODO: pomyśleć, w jaki sposób zrobić to lepiej
        if (optionalCollection.isPresent()) {
            WordsCollection collection = optionalCollection.get();
            return new CollectionResponseDTO(collection.getName(), collection.getDescription(), collection.getLearningLanguage().getAbbreviation(),
                    collection.getNativeLanguage().getAbbreviation(), String.valueOf(collection.getUser()),
                    collectionRepository.countWordsById(collection.getId())); // TODO: przerobić to, żeby było bardziej wydajne
//                    collection.getWordsCount());
        }
        return null;
//        return collectionRepository.findById(id);
    }

    public Optional<WordsCollection> getDetailsById(Long id) {
        throw new NotYetImplementedException();
    }


}
