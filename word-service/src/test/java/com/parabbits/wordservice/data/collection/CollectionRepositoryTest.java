package com.parabbits.wordservice.data.collection;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:initialization_db.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup_db.sql")
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CollectionRepositoryTest {

    @Data
    private static class CollectionTestData {

        private String name;
        private String description;
        private long user;
        private long language;
        private String abbr;

        public CollectionTestData(String name, String description, long user, long languageId, String abbr) {
            this.name = name;
            this.description = description;
            this.user = user;
            this.language = languageId;
            this.abbr = abbr;
        }
    }

    @Autowired
    private CollectionRepository repository;

    @Test
    public void shouldSaveCollection(){
        CollectionTestData data1 = new CollectionTestData("Test", "Test description", 1, 1, "eng");
        WordsCollection collection = createTestCollection(data1);
        testInsert(collection, data1);

        CollectionTestData data2 = new CollectionTestData("Test2", null, 1, 1, "eng");
        WordsCollection collection2 = createTestCollection(data2);
        testInsert(collection2, data2);
    }

    @Test
    public void shouldThrowInvalidDataException(){
        // throw exception, when name, user or language are empty
        // TODO: premyśleć, czy to jest dobry spobób
        Class<?> exceptionType = DataIntegrityViolationException.class;
        CollectionTestData data1 = new CollectionTestData(null, "Test description", 1, 1, "eng");
        WordsCollection collection = createTestCollection(data1);
        assertThatThrownBy(()->repository.save(collection)).isInstanceOf(exceptionType);

        CollectionTestData data2 = new CollectionTestData("Test", "Test description", -1, 1, "eng");
        WordsCollection collection2 = createTestCollection(data2);
        assertThatThrownBy(()->repository.save(collection2)).isInstanceOf(exceptionType);

        // TODO: sprawdzić,czy to będzie poprawne
        CollectionTestData data3 = new CollectionTestData("Test", "Test description", 1, 0, "eng");
        WordsCollection collection3 = createTestCollection(data3);
        assertThatThrownBy(()->repository.save(collection3)).isInstanceOf(exceptionType);

        CollectionTestData data4 = new CollectionTestData("Test", "Test description", 1, 2000, "eng");
        WordsCollection collection4 = createTestCollection(data4);
        assertThatThrownBy(()->repository.save(collection4)).isInstanceOf(exceptionType);
    }

    private WordsCollection createTestCollection(CollectionTestData data){
        WordsCollection collection = new WordsCollection();
        collection.setName(data.name);
        // TODO: jak to tutaj zrobić?
        // TODO: można by pobrać Language

        // TODO: sprawdzić, jak zrobić, aby wyrzuciło błąd zamiast wstawiać

        Language language = new Language();
        language.setId(data.language);
//        language.setName(data.language);
        language.setAbbreviation(data.abbr);
        collection.setLanguage(language);
        collection.setDescription(data.description);
        collection.setUser(data.user);

        return collection;
    }

    @Autowired
    private LanguageRepository languageRepository;

    private void testInsert(WordsCollection collection, CollectionTestData data){
        WordsCollection insertedCollection = repository.save(collection);
        Optional<WordsCollection> selectedCollection = repository.findById(insertedCollection.getId());
        assertTrue(selectedCollection.isPresent());
        assertEquals(data.name, selectedCollection.get().getName());
        assertEquals(data.description, selectedCollection.get().getDescription());
        assertEquals(data.user, selectedCollection.get().getUser());
//        assertEquals(data.language, selectedCollection.get().getLanguage().getName());
        assertEquals(data.abbr, selectedCollection.get().getLanguage().getAbbreviation());
    }

}
