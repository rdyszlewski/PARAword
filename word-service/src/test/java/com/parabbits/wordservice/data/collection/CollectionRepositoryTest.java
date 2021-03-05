package com.parabbits.wordservice.data.collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CollectionRepositoryTest {

    private class CollectionTestData {
        public String name;
        public String description;
        public long user;
        public String language;
        public String abbr;

        public CollectionTestData(String name, String description, long user, String language, String abbr) {
            this.name = name;
            this.description = description;
            this.user = user;
            this.language = language;
            this.abbr = abbr;
        }
    }

    @Autowired
    private CollectionRepository repository;

    @Test
    public void shouldSaveCollection(){
        CollectionTestData data1 = new CollectionTestData("Test", "Test description", 1, "English", "eng");
        WordsCollection collection = createTestCollection(data1);
        testInsert(collection, data1);

        CollectionTestData data2 = new CollectionTestData("Test2", null, 1, "English", "eng");
        WordsCollection collection2 = createTestCollection(data2);
        testInsert(collection2, data2);
    }

    @Test
    public void shouldThrowInvalidDataException(){
        // throw exception, when name, user or language are empty
        // TODO: premyśleć, czy to jest dobry spobób
        Class<?> exceptionType = DataIntegrityViolationException.class;
        CollectionTestData data1 = new CollectionTestData(null, "Test description", 1, "English", "eng");
        WordsCollection collection = createTestCollection(data1);
        assertThatThrownBy(()->repository.save(collection)).isInstanceOf(exceptionType);

        CollectionTestData data2 = new CollectionTestData("Test", "Test description", -1, "English", "eng");
        WordsCollection collection2 = createTestCollection(data2);
        assertThatThrownBy(()->repository.save(collection2)).isInstanceOf(exceptionType);

        CollectionTestData data3 = new CollectionTestData("Test", "Test description", 1, null, "eng");
        WordsCollection collection3 = createTestCollection(data3);
        assertThatThrownBy(()->repository.save(collection3)).isInstanceOf(exceptionType);
    }

    private WordsCollection createTestCollection(CollectionTestData data){
        WordsCollection collection = new WordsCollection();
        collection.setName(data.name);
        Language language = new Language();
        language.setName(data.language);
        language.setAbbreviation(data.abbr);
        collection.setLanguage(language);
        collection.setDescription(data.description);
        collection.setUser(data.user);

        return collection;
    }

    private void testInsert(WordsCollection collection, CollectionTestData data){
        WordsCollection insertedCollection = repository.save(collection);
        Optional<WordsCollection> selectedCollection = repository.findById(insertedCollection.getId());
        assertTrue(selectedCollection.isPresent());
        assertEquals(data.name, selectedCollection.get().getName());
        assertEquals(data.description, selectedCollection.get().getDescription());
        assertEquals(data.user, selectedCollection.get().getUser());
        assertEquals(data.language, selectedCollection.get().getLanguage().getName());
        assertEquals(data.abbr, selectedCollection.get().getLanguage().getAbbreviation());
    }

}
