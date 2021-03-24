package com.parabbits.wordservice.data.collection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class CollectionServiceTest {

    @MockBean
    private LanguageRepository languageRepository;

    @MockBean
    private CollectionRepository collectionRepository;

    @Test
    public void shouldReturnEntireCollection() {
//        long userId = 1;
//        List<WordsCollection> collections = collectionRepository.findByUser(userId);

    }
}
