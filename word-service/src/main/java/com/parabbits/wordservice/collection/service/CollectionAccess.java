package com.parabbits.wordservice.collection.service;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CollectionAccess {

    private final Boolean isPublic;
    private final Long ownerId;
    private boolean resourceExists;

    public CollectionAccess(Boolean isPublic, Long ownerId) {
        this.isPublic = isPublic;
        this.ownerId = ownerId;
        this.resourceExists = true;
    }

    private CollectionAccess(boolean exists, Boolean isPublic, Long ownerId) {
        this(isPublic, ownerId);
        this.resourceExists = exists;
    }

    public static CollectionAccess notFound() {
        return new CollectionAccess(false, null, null);
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

    public static CollectionAccess ownerAccess(long userId) {
        return new CollectionAccess(true, false, userId);
    }

    public static CollectionAccess getAccess(Boolean isPublic, Long userId) {
        return new CollectionAccess(true, isPublic, userId);
    }

    public boolean exist() {
        return resourceExists;
    }

    // TODO: stworzyć obiekt, który będzie ok i ktry nie będzie

}
