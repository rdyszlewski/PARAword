package com.parabbits.wordservice.collection.accessService;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class CollectionAccessHandler {

    public static void handle(AccessResult result) {
        switch (result) {
            case FORBIDDEN:
                throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
            case UNAUTHORIZED:
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
            case NOT_FOUND:
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }
}
