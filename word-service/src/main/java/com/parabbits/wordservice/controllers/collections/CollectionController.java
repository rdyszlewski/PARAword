package com.parabbits.wordservice.controllers.collections;

import com.parabbits.wordservice.data.collection.CollectionService;
import com.parabbits.wordservice.data.collection.Language;
import com.parabbits.wordservice.data.collection.WordsCollection;
import com.parabbits.wordservice.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/collection", produces = "application/json")
public class CollectionController {

    // TODO: pozmieniać błędy

    private CollectionService collectionService;

    @Autowired
    public CollectionController(CollectionService collectionService){
        this.collectionService = collectionService;
    }

    @GetMapping(path = "/user/{userId}")
    @PreAuthorize("#user.getId().equals(#userId)")
    public List<CollectionDTO> getCollectionsByUser(@PathVariable long userId, @AuthenticationPrincipal User user){

        List<WordsCollection> collections =  collectionService.getAllCollections(userId);
        return collections.stream().map(x->new CollectionDTO(x.getName(), x.getDescription(), x.getLanguage().getAbbreviation())).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public CollectionDTO getCollectionsById(@PathVariable long id, @AuthenticationPrincipal User user){
        Optional<WordsCollection> collection = collectionService.getById(id);
        if(collection.isPresent()){
            if(collection.get().getUser() != user.getId()){
                // TODO: wypełnić błąd
                throw new AuthorizationServiceException("");
            }
        }
        return collection.map(wordsCollection -> new CollectionDTO(wordsCollection.getName(), wordsCollection.getDescription(), wordsCollection.getLanguage().getAbbreviation())).orElse(null);
        // TODO: tutaj można chyba wyrzucić jakiś wyjątek w przypadku nieznalezienia zasobu
    }

    @PostMapping
    public void insertCollections(@RequestBody CollectionDTO collectionDTO, @AuthenticationPrincipal User user){
        WordsCollection collection = new WordsCollection();
        collection.setName(collectionDTO.getName());
        collection.setDescription(collectionDTO.getDescription());
        Optional<Language> language = collectionService.getLanguageByAbbreviation(collectionDTO.getLanguage());
        collection.setLanguage(language.orElse(null)); // TODO: zastanowić się, czy to może być nullem
        collection.setUser(user.getId()); // TODO: to będzie trzeba jakoś zmienić
        collectionService.addCollection(collection);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteCollections(@PathVariable long id, @AuthenticationPrincipal User user){
        Optional<WordsCollection> wordsCollection = collectionService.getById(id);
        if(wordsCollection.isPresent()){
            if(wordsCollection.get().getUser() != user.getId()){
                throw new AuthorizationServiceException("");
            }
            collectionService.removeCollection(id);
            // TODO: zrobić zwracanie odpowiednich kodów
        }
    }

    @PutMapping(path = "/{id}")
    public void updateCollection(@PathVariable long id, @RequestBody CollectionDTO collectionDTO, @AuthenticationPrincipal User user){
        Optional<WordsCollection> wordsCollection = collectionService.getById(id);
        if(wordsCollection.isPresent()){
            WordsCollection collection = wordsCollection.get();
            if(wordsCollection.get().getUser() != user.getId()){
                throw new AuthorizationServiceException("");
            }
            collection.setName(collectionDTO.getName());
            collection.setDescription(collectionDTO.getDescription());
            Optional<Language> language = collectionService.getLanguageByAbbreviation(collectionDTO.getLanguage());
            collection.setLanguage(language.orElse(collection.getLanguage()));

            collectionService.updateCollection(collection);
            // TODO: tutaj może być potrzebny jakiś błąd, w przypadku, jeżeli podane dane języka są nieprawidłowe

        }
    }
 }
