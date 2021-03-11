package com.parabbits.wordservice.data.word;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:initialization_db.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup_db.sql")
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TranslationRepositoryTest {

    @Autowired
    private TranslationRepository repository;

    @Test
    public void shouldFindById(){
        Optional<Translation> dogTranslation = repository.findById(1L);
        assertThat(dogTranslation.isPresent()).isTrue();
        testTranslation(dogTranslation.get(), "pies", "Piesek",1, Arrays.asList("dog", "hound"));

        Optional<Translation> mouseTranslation = repository.findById(4L);
        assertThat(mouseTranslation.isPresent()).isTrue();
        testTranslation(mouseTranslation.get(), "mysz", null,0, Collections.singletonList("mouse"));
    }

    private void testTranslation(Translation translation, String name, String description, int meaning, List<String> words){
        assertThat(translation.getName()).isEqualTo(name);
        assertThat(translation.getDescription()).isEqualTo(description);
        assertThat(translation.getMeaning()).isEqualTo(meaning);
        testWordsFetchMode(translation.getWords(), true);
        assertThat(translation.getWords()).isNotEmpty();
        assertThat(translation.getWords().size()).isEqualTo(words.size());
        List<String> wordsNames = translation.getWords().stream().map(Word::getWord).collect(Collectors.toList());;
        assertThat(wordsNames).containsAll(words);
    }

    private void testWordsFetchMode(Set<Word> words, boolean shouldBeInitialized){
        assertThat(Hibernate.isInitialized(words)).isEqualTo(shouldBeInitialized);
    }

    @Test
    public void shouldCheckThatTranslationExists(){
        boolean dogExists = repository.existsByWordAndName(1L, "pies");
        assertThat(dogExists).isTrue();

        boolean monkeyExists = repository.existsByWordAndName(1L, "małpa");
        assertThat(monkeyExists).isFalse();
    }

    @Test
    public void shouldFindByWord(){
        testFindByWord(1L, Collections.singletonList("pies"));
        testFindByWord(4L, Arrays.asList("mysz", "myszka", "mysz"));
    }

    private void testFindByWord(long wordId, List<String> expectedTranslations){
        List<Translation> translations = repository.findByWord(wordId);
        assertThat(translations.size()).isEqualTo(expectedTranslations.size());
        List<String> names = translations.stream().map(Translation::getName).collect(Collectors.toList());
        assertThat(names).containsAll(expectedTranslations);
        for(Translation translation: translations){
            testWordsFetchMode(translation.getWords(), false);
        }
    }

    @Test
    public void shouldFindByFilter(){
        TranslationFilter nameFilter = TranslationFilter.builder(1L).name("pies").build();
        testFindByFilter(nameFilter, Collections.singletonList("pies"));

        TranslationFilter meaningFilter = TranslationFilter.builder(1L).meaning(1).build();
        testFindByFilter(meaningFilter, Arrays.asList("pies", "myszka"));

        TranslationFilter meaningFilter2 = TranslationFilter.builder(1L).meaning(0).build();
        testFindByFilter(meaningFilter2, Arrays.asList("dogryźć", "kot", "mysz"));

        TranslationFilter nameMeaningFilter = TranslationFilter.builder(1L).name("pies").meaning(1).build();
        List<Translation> translations = testFindByFilter(nameMeaningFilter, Collections.singletonList("pies"));
        assertThat(translations.get(0).getMeaning()).isEqualTo(1);

        TranslationFilter descriptionFilter = TranslationFilter.builder(1L).description("long").build();
        testFindByFilter(descriptionFilter, Arrays.asList("kot", "myszka"));

        TranslationFilter descriptionFilter2 = TranslationFilter.builder(1L).description("Long").build();
        testFindByFilter(descriptionFilter2, Arrays.asList("kot", "myszka"));

        TranslationFilter wordFilter = TranslationFilter.builder(1L).word("dog").build();
        testFindByFilter(wordFilter, Arrays.asList("pies", "dogryźć"));

        TranslationFilter posFilter = TranslationFilter.builder(1L).partOfSpeech(PartOfSpeech.NOUN).build();
        testFindByFilter(posFilter, Arrays.asList("pies", "dogryźć"));

        TranslationFilter userFilter = TranslationFilter.builder(2L).build();
        testFindByFilter(userFilter, Arrays.asList("kaczka", "telefon", "mysz"));

        TranslationFilter collectionFilter = TranslationFilter.builder(1L).collectionId(1).build();
        testFindByFilter(collectionFilter, Arrays.asList("pies", "dogryźć", "kot", "mysz", "myszka"));

        TranslationFilter collectionFilter2 = TranslationFilter.builder(2L).collectionId(2).build();
        testFindByFilter(collectionFilter2, Arrays.asList("kaczka", "telefon"));

        TranslationFilter collectionFilter3 = TranslationFilter.builder(2L).collectionId(1).build();
        testFindByFilter(collectionFilter3, Arrays.asList("mysz"));
    }

    private List<Translation> testFindByFilter(TranslationFilter filter, List<String> expectedTranslations){
        TranslationSpecifications specifications = new TranslationSpecifications();
        List<Translation> translations = repository.findAll(TranslationSpecifications.getFilterSpecification(filter));
        assertThat(translations.size()).isEqualTo(expectedTranslations.size());
        List<String> translationsNames = translations.stream().map(Translation::getName).collect(Collectors.toList());
        assertThat(translationsNames).containsAll(expectedTranslations);
        return translations;
    }

}
