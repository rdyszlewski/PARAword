package com.parabbits.wordservice.collection.controller;

import com.parabbits.wordservice.collection.accessService.AccessResult;
import com.parabbits.wordservice.collection.accessService.CollectionAccessHandler;
import com.parabbits.wordservice.collection.accessService.CollectionAccessService;
import com.parabbits.wordservice.collection.accessService.UserAction;
import com.parabbits.wordservice.collection.data.CollectionFilter;
import com.parabbits.wordservice.collection.service.CollectionAccess;
import com.parabbits.wordservice.collection.service.CollectionDTO;
import com.parabbits.wordservice.collection.service.CollectionResponseDTO;
import com.parabbits.wordservice.collection.service.CollectionService;
import com.parabbits.wordservice.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "collection", produces = "application/json")
public class CollectionController {

    private final CollectionService service;
    private final CollectionAccessService accessService;

    @Autowired
    public CollectionController(CollectionService collectionService, CollectionAccessService accessService) {
        this.service = collectionService;
        this.accessService = accessService;
    }

    @GetMapping("{id}")
    public CollectionResponseDTO getCollectionById(@PathVariable long id, @AuthenticationPrincipal UserPrincipal principal) {
        checkAccess(id, principal, UserAction.GET);
        return service.getById(id);
    }

    private void checkAccess(long id, UserPrincipal principal, UserAction action) {
        handleResult(accessService.checkAccess(id, principal, action));
    }

    @GetMapping("")
    public List<CollectionResponseDTO> getAllCollections(
            @RequestParam(required = false) String name, @RequestParam(required = false) String description,
            @RequestParam(required = false) Long language1, @RequestParam(required = false) Long language2,
            @RequestParam(required = false) Long userId, @RequestParam(required = false) Boolean isPublic
            , @AuthenticationPrincipal UserPrincipal principal
    ) {
        checkAccess(CollectionAccess.getAccess(isPublic, userId), principal, UserAction.GET);
        CollectionFilter filter = CollectionFilter.builder().name(name).description(description).language1(language1)
                .language2(language2).userId(userId).publicCollection(isPublic).build();
        return service.getByFilter(filter);
    }

    private void handleResult(AccessResult result) {
        if (!result.equals(AccessResult.OK)) {
            CollectionAccessHandler.handle(result);
        }
    }

    private void checkAccess(CollectionAccess access, UserPrincipal principal, UserAction action) {
        handleResult(accessService.checkAccess(access, principal, action));
    }

    @PostMapping
    public CollectionResponseDTO addCollection(@RequestBody CollectionDTO dto, @AuthenticationPrincipal UserPrincipal principal) {
        checkAccess(CollectionAccess.ownerAccess(dto.getUserId()), principal, UserAction.CREATE);
        return service.addCollection(dto);
    }

    @DeleteMapping("/{id}")
    public void removeCollection(@PathVariable long id, @AuthenticationPrincipal UserPrincipal principal) {
        checkAccess(id, principal, UserAction.REMOVE);
        service.removeCollection(id);
    }

    @PutMapping("/{id}")
    public void updateCollection(@PathVariable long id, @RequestBody CollectionDTO body, @AuthenticationPrincipal UserPrincipal principal) {
        checkAccess(id, principal, UserAction.UPDATE);
        body.setId(id);
        // TODO: wypadałoby sprawdzić, czy kolekacja nie zmienia właściciela
        service.updateCollection(body);
    }
}
