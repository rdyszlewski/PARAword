package com.parabbits.wordservice.controllers.collections;

import com.parabbits.wordservice.collection.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/collection", produces = "application/json", consumes = "application/json")
public class CollectionController {

    private CollectionService collectionService;

    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }
    
}
