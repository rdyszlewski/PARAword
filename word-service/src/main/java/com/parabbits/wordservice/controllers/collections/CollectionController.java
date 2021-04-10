package com.parabbits.wordservice.controllers.collections;

import com.parabbits.wordservice.collection.data.CollectionFilter;
import com.parabbits.wordservice.collection.service.CollectionAccess;
import com.parabbits.wordservice.collection.service.CollectionDTO;
import com.parabbits.wordservice.collection.service.CollectionResponseDTO;
import com.parabbits.wordservice.collection.service.CollectionService;
import com.parabbits.wordservice.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;


@RestController
@RequestMapping(path = "collection", produces = "application/json")
public class CollectionController {


    private CollectionService service;

    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.service = collectionService;
    }

    @GetMapping("{id}")
    public CollectionResponseDTO getCollectionById(@PathVariable long id, @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        CollectionResponseDTO result = service.getById(id);
        checkAccessToObject(result, principal);
        return result;
    }


    private void checkAccessToObject(CollectionResponseDTO responseDTO, UserPrincipal principal) {
        if (responseDTO == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        if (!hasAccessToCollection(principal, responseDTO)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean hasAccessToCollection(UserPrincipal principal, CollectionResponseDTO result) {
        return result.getIsPublic() || principal.getId() == getUserId(result);
    }

    private long getUserId(CollectionResponseDTO result) {
        return Long.parseLong(result.getUserName());
    }


    @GetMapping("")
    public List<CollectionResponseDTO> getAllCollections(
            @RequestParam(required = false) String name, @RequestParam(required = false) String description,
            @RequestParam(required = false) Long language1, @RequestParam(required = false) Long language2,
            @RequestParam(required = false) Long userId, @RequestParam(required = false) Boolean isPublic
            , @AuthenticationPrincipal UserPrincipal principal
    ) {
        if (principal == null || ((isPublic == null || !isPublic) && (userId != null && !userId.equals(principal.getId())))) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        // TODO: dodać sprawdzanie, czy użytkownik ma możliwość pobrać dane
        CollectionFilter filter = CollectionFilter.builder().name(name).description(description).language1(language1)
                .language2(language2).userId(userId).publicCollection(isPublic).build();
        return service.getByFilter(filter);
    }

    @PostMapping
    public CollectionResponseDTO addCollection(@RequestBody CollectionDTO dto, @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null || !principal.getId().equals(dto.getUserId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }

        return service.addCollection(dto);
    }

    @DeleteMapping("/{id}")
    public void removeCollection(@PathVariable long id, @AuthenticationPrincipal UserPrincipal principal) {
        checkCollectionOwner(id, principal);
        service.removeCollection(id);
    }

    private void checkCollectionOwner(long collectionId, UserPrincipal principal) {
        if (principal == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        CollectionAccess access = service.getCollectionAccess(collectionId);
        if (!access.exist()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        } else if (!access.isOwner(principal.getId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public void updateCollection(@PathVariable long id, @RequestBody CollectionDTO body, @AuthenticationPrincipal UserPrincipal principal) {
        checkCollectionOwner(id, principal);
        body.setId(id);
        // TODO: wypadałoby sprawdzić, czy kolekacja nie zmienia właściciela
        service.updateCollection(body);
    }


}
