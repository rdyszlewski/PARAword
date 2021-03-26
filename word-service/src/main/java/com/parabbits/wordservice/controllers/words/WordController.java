package com.parabbits.wordservice.controllers.words;

import com.parabbits.wordservice.collection.service.CollectionService;
import com.parabbits.wordservice.data.word.WordService;
import com.parabbits.wordservice.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "word",produces = "application/json")
public class WordController {

    private WordService wordService;
    private CollectionService collectionService;

    @Autowired
    public WordController(WordService wordService, CollectionService collectionService){
        this.wordService =wordService;
        this.collectionService = collectionService;
    }

    @GetMapping("/{id}")
    public SimpleWordEntryDTO getWord(@PathVariable long id) {
//        Optional<Word> foundedWord = wordService.getWordById(id);
//        Word word = foundedWord.orElse(new Word());
//        List<String> translations = word.getTranslations().stream().map(Translation::getName).collect(Collectors.toList());
//        return new SimpleWordEntryDTO(word.getId(), word.getWord(), translations);
        return new SimpleWordEntryDTO(1, "Siemano", new ArrayList<>());
    }


    @PostMapping("/{collectionId}")
    public WordDTO createNewWord(@RequestBody WordDTO wordDTO, @PathVariable long collectionId, @AuthenticationPrincipal User user){
//        // TODO: TODO: te wszystkie rzeczy sprawdzające można przenieść do apektów
//        Optional<WordsCollection> optionalCollection = collectionService.getById(collectionId);
//        if(optionalCollection.orElseThrow(IllegalArgumentException::new).getUser() != user.getId()){
//            throw new AuthorizationServiceException(""); // TODO: dorobić komunikat
//        }
//        Word word = new Word();
//        word.setWord(wordDTO.getWord());
//        word.setDescription(wordDTO.getDescription());
//        word.setPartOfSpeech(wordDTO.getPartOfSpeech());
//        word.setCollection(optionalCollection.get()); // TODO: sprawdzić, czy to będzie ok
//
//        Optional<Word> insertedWord = wordService.saveWord(word);
//        if(insertedWord.isPresent()){
//            wordDTO.setId(insertedWord.get().getId());
//            return wordDTO;
//        }
        // TODO: tutaj wyświetlić jakiś komunikat o błędzie
        return null;
    }

    // TODO: tutaj musi być inny zasób
    @PostMapping("/entry/{collectionId}")
    public WordEntryDTO addWord(@RequestBody WordEntryDTO wordEntry, @PathVariable long collectionId, @AuthenticationPrincipal User user) {
//        // TODO: zastanowić się, czy collectionId powinno być w zasobie, czy może przenieść to do body
//        Optional<WordsCollection> optionalCollection = collectionService.getById(collectionId);
//        if(optionalCollection.orElseThrow(IllegalArgumentException::new).getUser() != user.getId()){
//            throw new AuthorizationServiceException(""); // TODO: dorobić komunikat
//        }

        return null;
    }


    // TODO: dodawanie nowego znaczenia
    // TODO: dodawanie słowa z tłumaczeniem
    // TODO: pobieranie słowa po id z tłumaczeniami
    // TODO: pobieranie tłumaczenia z powiązanymi słowami
    // TODO: pobieranie listy słów użytkownika
    // TODO; pobieranie prostego słowa na podstawie nazwy
    // TODO: pobieranie pełnego słowa na podstawie nazwy
    // TODO: pobieranie słowa na podstawie filtra
    // TODO: pobieranie losowych słów
}
