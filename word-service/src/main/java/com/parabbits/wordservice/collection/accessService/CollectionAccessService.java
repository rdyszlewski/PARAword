package com.parabbits.wordservice.collection.accessService;

import com.parabbits.wordservice.collection.service.CollectionAccess;
import com.parabbits.wordservice.collection.service.CollectionService;
import com.parabbits.wordservice.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionAccessService {

    private final CollectionService service;

    @Autowired
    public CollectionAccessService(CollectionService service) {
        this.service = service;
    }

    public AccessResult checkAccess(CollectionAccess access, UserPrincipal principal, UserAction action) {
        if (!access.exist()) {
            return AccessResult.NOT_FOUND;
        }
        if (!hasAccessToCollection(principal, access, action)) {
            return AccessResult.FORBIDDEN;
        }
        return AccessResult.OK;
    }

    private boolean hasAccessToCollection(UserPrincipal principal, CollectionAccess access, UserAction action) {
        switch (action) {
            case CREATE:
            case UPDATE:
            case REMOVE:
                return access.isOwner(principal.getId());
            case GET:
                return access.canAccess(principal.getId());
        }
        return false;
    }

    public AccessResult checkAccess(Long collectionId, UserPrincipal principal, UserAction action) {
        if (principal == null) {
            return AccessResult.UNAUTHORIZED;
        }
        CollectionAccess access = service.getCollectionAccess(collectionId);
        return checkAccess(access, principal, action);
    }

}
