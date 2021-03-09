package com.parabbits.wordservice.data.word;

import org.assertj.core.api.AssertionsForClassTypes;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:initialization_db.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup_db.sql")
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WordRepositoryTest {

    @Autowired
    private WordRepository repository;
    

    @Test
    public void shouldFindById(){
        testWord(1L, "dog", Collections.singletonList("pies"));
        testWord(4L, "mouse", Arrays.asList("mysz", "myszka", "mysz"));
    }

    private void testWord(final long id, final String word,final List<String> translations)
    {
        Optional<Word> optionalWord = repository.findById(id);
        assertThat(optionalWord.isPresent()).isTrue();
        testWord(optionalWord.get(), id, word, translations);
    }

    private void testWord(Word word, long id, String name, List<String> translations){
        AssertionsForClassTypes.assertThat(word.getWord()).isEqualTo(name);
        AssertionsForClassTypes.assertThat(word.getId()).isEqualTo(id);
        AssertionsForClassTypes.assertThat(word.getCollection()).isNotNull();
        assertThat(word.getTranslations()).isNotEmpty();
        testTranslationsFetchMode(word.getTranslations(), true);
        AssertionsForClassTypes.assertThat(word.getTranslations().size()).isEqualTo(translations.size());
        List<String> foundedTranslations = word.getTranslations().stream().map(Translation::getName).collect(Collectors.toList());
        assertThat(foundedTranslations).containsAll(translations);
    }

    @Test
    public void shouldFindByCollection(){
        final boolean TRANSLATIONS_SHOULD_BY_EAGER = false;
        testCollection(1, Arrays.asList("dog", "dog", "cat", "mouse"), TRANSLATIONS_SHOULD_BY_EAGER);
        testCollection(2, Arrays.asList("duck", "phone"), TRANSLATIONS_SHOULD_BY_EAGER);
    }

    private void testCollection(long collectionId, List<String> expectedWords, boolean shouldBeEager){
        List<Word> words = repository.findByCollection(collectionId);
        assertThat(words.size()).isEqualTo(expectedWords.size());
        List<String> wordNames = words.stream().map(Word::getWord).collect(Collectors.toList());
        assertThat(wordNames).containsAll(expectedWords);
        testAllTranslationsFetchMode(words, shouldBeEager);
    }

    private void testTranslationsFetchMode(Set<Translation> translations, boolean shouldBeInitialized){
        assertThat(Hibernate.isInitialized(translations)).isEqualTo(shouldBeInitialized);
    }

    private void testAllTranslationsFetchMode(List<Word> words, boolean isEager){
        for(Word word: words){
            testTranslationsFetchMode(word.getTranslations(), isEager);
        }
    }

    @Test
    public void shouldFindByFilter(){
        WordFilter userFilter1 = WordFilter.builder(1L).build();
        testFindByFilter(userFilter1, Arrays.asList("dog", "dog", "car", "mouse"));

        WordFilter userFilter2 = WordFilter.builder(2L).build();
        testFindByFilter(userFilter2, Arrays.asList("duck", "phone"));

        WordFilter wordFilter = WordFilter.builder(1L).name("dog").build();
        testFindByFilter(wordFilter, Arrays.asList("dog", "dog"));

        WordFilter collectionFilter = WordFilter.builder(1L).collectionId(1L).build();
        testFindByFilter(collectionFilter, Arrays.asList("dog", "dog", "car", "mouse"));

        WordFilter emptyCollectionFilter = WordFilter.builder(1L).collectionId(2L).build();
        testFindByFilter(emptyCollectionFilter, Collections.emptyList());

        WordFilter posFilter = WordFilter.builder(1L).partOfSpeech(PartOfSpeech.PRONOUN).build();
        testFindByFilter(posFilter, Arrays.asList("dog", "dog", "mouse"));
    }

    private void testFindByFilter(WordFilter filter, List<String> expectedWords){
        List<Word> words = repository.findAll(WordSpecifications.filterWords(filter));
        assertThat(words.size()).isEqualTo(expectedWords.size());
        testAllTranslationsFetchMode(words, true);
    }

}
