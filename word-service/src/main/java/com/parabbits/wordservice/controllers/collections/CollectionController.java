package com.parabbits.wordservice.controllers.collections;

import com.parabbits.wordservice.collection.data.CollectionFilter;
import com.parabbits.wordservice.collection.service.CollectionDTO;
import com.parabbits.wordservice.collection.service.CollectionResponseDTO;
import com.parabbits.wordservice.collection.service.CollectionService;
import com.parabbits.wordservice.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "collection", produces = "application/json")
public class CollectionController {

    private final ResponseEntity<CollectionResponseDTO> EMPTY_RESPONSE;

    private CollectionService service;

    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.service = collectionService;
        EMPTY_RESPONSE = new ResponseEntity<>(null, HttpStatus.OK);
    }

    // TODO: pobieranie servisu

    @GetMapping("{id}")
    public ResponseEntity<CollectionResponseDTO> getCollectionById(@PathVariable long id, @AuthenticationPrincipal UserPrincipal principal) {
        CollectionResponseDTO result = service.getById(id);
        if (result == null || principal == null) {
            return EMPTY_RESPONSE;
        }
        if (result.getIsPublic() || principal.getId() == Long.parseLong(result.getUserName())) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("")
    public List<CollectionResponseDTO> getAllCollections(
            @RequestParam(required = false) String name, @RequestParam(required = false) String description,
            @RequestParam(required = false) Long language1, @RequestParam(required = false) Long language2,
            @RequestParam(required = false) Long userId, @RequestParam(required = false) Boolean isPublic
            , @AuthenticationPrincipal UserPrincipal principal
    ) {
        // TODO: dodać sprawdzanie, czy użytkownik ma możliwość pobrać dane
        CollectionFilter filter = CollectionFilter.builder().name(name).description(description).language1(language1)
                .language2(language2).userId(userId).publicCollection(isPublic).build();
        return service.getByFilter(filter);
    }

    @PostMapping
    public CollectionResponseDTO addCollection(@RequestBody CollectionDTO dto) {
        // TODO: czy trzeba sprawdzać, czy użytkownik ma prawo do zapisu.
        return service.addCollection(dto);
    }

}
