package com.parabbits.wordservice.controllers.collections;

import com.parabbits.wordservice.data.collection.CollectionService;
import com.parabbits.wordservice.data.collection.Language;
import com.parabbits.wordservice.data.collection.WordsCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/collection", produces = "application/json")
public class CollectionController {

    private CollectionService collectionService;

    @Autowired
    public CollectionController(CollectionService collectionService){
        this.collectionService = collectionService;
    }

    @GetMapping(path = "/user/{userId}")
    public List<CollectionDTO> getCollectionsByUser(@PathVariable long userId){
        List<WordsCollection> collections =  collectionService.getAllCollections(userId);
        return collections.stream().map(x->new CollectionDTO(x.getName(), x.getDescription(), x.getLanguage().getAbbreviation())).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public CollectionDTO getCollectionsById(@PathVariable long id){
        Optional<WordsCollection> collection = collectionService.getById(id);
        return collection.map(wordsCollection -> new CollectionDTO(wordsCollection.getName(), wordsCollection.getDescription(), wordsCollection.getLanguage().getAbbreviation())).orElse(null);
        // TODO: tutaj można chyba wyrzucić jakiś wyjątek w przypadku nieznalezienia zasobu
    }

    @PostMapping
    public void insertCollections(@RequestBody CollectionDTO collectionDTO){
        WordsCollection collection = new WordsCollection();
        collection.setName(collectionDTO.getName());
        collection.setDescription(collectionDTO.getDescription());
        Optional<Language> language = collectionService.getLanguageByAbbreviation(collectionDTO.getLanguage());
        collection.setLanguage(language.orElse(null)); // TODO: zastanowić się, czy to może być nullem
        collection.setUser(3); // TODO: to będzie trzeba jakoś zmienić
        collectionService.addCollection(collection);
    }

}
