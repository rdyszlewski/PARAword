package com.parabbits.wordservice.controllers.words;

import com.parabbits.wordservice.data.word.Word;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "word",produces = "application/json")
public class WordController {

    @GetMapping("/{id}")
    public Word getWord(@PathVariable long id){
        return new Word();
    }
}
