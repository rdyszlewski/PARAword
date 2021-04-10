package com.parabbits.wordservice.collection.service;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CollectionAccess {

    private Boolean isPublic;
    private Long ownerId;

    public CollectionAccess(Boolean isPublic, Long ownerId) {
        this.isPublic = isPublic;
        this.ownerId = ownerId;
    }

    public boolean exist() {
        return ownerId != null;
    }

    public boolean isOwner(long userId) {
        if (!exist()) {
            return false;
        }
        return ownerId.equals(userId);
    }

    public boolean canAccess(long userId) {
        if (!exist()) {
            return false;
        }
        return isPublic || ownerId.equals(userId);
    }
}
