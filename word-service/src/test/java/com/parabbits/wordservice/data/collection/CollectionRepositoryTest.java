package com.parabbits.wordservice.data.collection;

import com.parabbits.wordservice.collection.data.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private CollectionRepository repository;
    @Autowired
    private LanguageRepository languageRepository;

    @Test
    public void shouldSaveCollection() {
        CollectionTestData data1 = new CollectionTestData("Test", "Test description", 1, 1, 3, "eng", "pl");
        WordsCollection collection = createTestCollection(data1);
        testInsert(collection, data1);

        CollectionTestData data2 = new CollectionTestData("Test2", null, 1, 1, 3, "eng", "pl");
        WordsCollection collection2 = createTestCollection(data2);
        testInsert(collection2, data2);
    }

    @Test
    public void shouldThrowInvalidDataException() {
        // throw exception, when name, user or language are empty
        // TODO: premyśleć, czy to jest dobry spobób
        Class<?> exceptionType = DataIntegrityViolationException.class;
        CollectionTestData data1 = new CollectionTestData(null, "Test description", 1, 1, 3, "eng", "pl");
        WordsCollection collection = createTestCollection(data1);
        assertThatThrownBy(() -> repository.save(collection)).isInstanceOf(exceptionType);

        CollectionTestData data2 = new CollectionTestData("Test", "Test description", -1, 1, 3, "eng", "pl");
        WordsCollection collection2 = createTestCollection(data2);
        assertThatThrownBy(() -> repository.save(collection2)).isInstanceOf(exceptionType);

        // TODO: sprawdzić,czy to będzie poprawne
        CollectionTestData data3 = new CollectionTestData("Test", "Test description", 1, 0, 3, "eng", "pl");
        WordsCollection collection3 = createTestCollection(data3);
        assertThatThrownBy(() -> repository.save(collection3)).isInstanceOf(exceptionType);

        CollectionTestData data4 = new CollectionTestData("Test", "Test description", 1, 2000, 3, "eng", "pl");
        WordsCollection collection4 = createTestCollection(data4);
        assertThatThrownBy(() -> repository.save(collection4)).isInstanceOf(exceptionType);

        // można dodać testy, które sprawdzają, czy language2 ma poprawną wartość
    }

    private WordsCollection createTestCollection(CollectionTestData data) {
        WordsCollection collection = new WordsCollection();
        collection.setName(data.name);

        Language language1 = new Language(data.language1, "LearningLanguage", data.languageAbbr1);
        collection.setLearningLanguage(language1);

        Language language2 = new Language(data.language2, "NativeLanguage", data.languageAbbr2);
        collection.setNativeLanguage(language2);

        collection.setDescription(data.description);
        collection.setUser(data.user);

        return collection;
    }

    private void testInsert(WordsCollection collection, CollectionTestData data) {
        WordsCollection insertedCollection = repository.save(collection);
        Optional<WordsCollection> selectedCollection = repository.findById(insertedCollection.getId());
        assertTrue(selectedCollection.isPresent());
        assertEquals(data.name, selectedCollection.get().getName());
        assertEquals(data.description, selectedCollection.get().getDescription());
        assertEquals(data.user, selectedCollection.get().getUser());
//        assertEquals(data.language, selectedCollection.get().getLanguage().getName());
        assertEquals(data.languageAbbr1, selectedCollection.get().getLearningLanguage().getAbbreviation());
        // TODO: chcemy przetestować, czy słowa są zrobioone leniwie czy nie
    }

    @Test
    public void shouldFindByFilter() {
        CollectionFilter userFilter = CollectionFilter.builder().userId(2).build();
        testFilter(userFilter, Arrays.asList("Angielski 2", "Publiczny"));
        CollectionFilter nameFilter = CollectionFilter.builder().name("Angielski").userId(1).build();
        testFilter(nameFilter, Arrays.asList("Angielski 1", "Angielski 3"));

        CollectionFilter descriptionFilter = CollectionFilter.builder().description("kolekcja").userId(1).build();
        testFilter(descriptionFilter, Arrays.asList("Niemiecki 1", "Niemiecki 2", "Angielski 3"));

        CollectionFilter language1Filter = CollectionFilter.builder().language1(2).userId(1).build();
        testFilter(language1Filter, Arrays.asList("Niemiecki 1", "Niemiecki 2", "Niemiecki osiem"));

        CollectionFilter language2Filter = CollectionFilter.builder().language2(2).userId(1).build();
        testFilter(language2Filter, Arrays.asList("Angielski 3", "Niemiecki osiem"));

        CollectionFilter allPublicFilter = CollectionFilter.builder().publicCollection(true).build();
        testFilter(allPublicFilter, Arrays.asList("Niemiecki 1", "Niemiecki 2", "Angielski 3", "Niemiecki osiem", "Publiczny"));

        CollectionFilter userPublicFilter = CollectionFilter.builder().publicCollection(true).userId(2).build();
        testFilter(userPublicFilter, Collections.singletonList("Publiczny"));
    }

    private void testFilter(CollectionFilter filter, List<String> expected) {
        List<WordsCollection> collections = repository.findAll(CollectionSpecification.getSpecification(filter));
        assertThat(collections.size()).isEqualTo(expected.size());
        List<String> collectionsNames = collections.stream().map(WordsCollection::getName).collect(Collectors.toList());
        assertThat(collectionsNames).containsAll(expected);
    }

    @Test
    public void shouldReturnPublicWhenIdIsNotSet() {
        CollectionFilter simpleFilter = CollectionFilter.builder().publicCollection(false).build();
        testFilter(simpleFilter, Arrays.asList("Niemiecki 1", "Niemiecki 2", "Angielski 3", "Niemiecki osiem", "Publiczny"));
    }

    @Data
    @AllArgsConstructor
    private static class CollectionTestData {

        private String name;
        private String description;
        private long user;
        private long language1;
        private long language2;
        private String languageAbbr1;
        private String languageAbbr2;
    }

}
