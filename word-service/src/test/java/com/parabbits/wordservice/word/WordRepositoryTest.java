package com.parabbits.wordservice.word;

import com.parabbits.wordservice.collection.data.Language;
import com.parabbits.wordservice.collection.data.WordsCollection;
import com.parabbits.wordservice.word.data.common.PartOfSpeech;
import com.parabbits.wordservice.word.data.translation.Translation;
import com.parabbits.wordservice.word.data.word.Word;
import com.parabbits.wordservice.word.data.word.WordFilter;
import com.parabbits.wordservice.word.data.word.WordRepository;
import com.parabbits.wordservice.word.data.word.WordSpecifications;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.hibernate.Hibernate;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:initialization_db.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup_db.sql")
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WordRepositoryTest {

    // TODO: 4. wstawianie nowego słowa - do wstawienia potrzebne jest chyba jakieś tłumaczenie
    // TODO: 5. wstawienie słowa bez tłumaczenia - to można przenieść do serwisu
    // TODO: 6. próba wstawienia słowa z niepoprawnymi danymi lub bez nich, lub z duplikatem nazwy
    // TODO: 7. aktualizacja słowa
    // TODO: 8. próba usunięcia lub zmieniania tłumaczenia dla słowa. Powinno być niepoprawne
    // TODO: 9. usunięcie słowa z bazy - sprawdzić, czy tłumaczenia również zostaną usunięte. Sprawdzić, czy jeżeli tłumaczenie jest przypisane do kilku słów, też zostanie usunięte

    @Autowired
    private WordRepository repository;

    @Test
    public void shouldGetById() {
        testFindById(1L, "dog", Collections.singletonList("pies"));
        testFindById(4L, "mouse", Arrays.asList("mysz", "myszka", "mysz"));

        Optional<Word> notExistsWord = repository.findById(200L);
        assertThat(notExistsWord).isNotPresent();

    }

    private void testFindById(final long id, final String word, final List<String> translations) {
        Optional<Word> optionalWord = repository.findById(id);
        AssertionsForClassTypes.assertThat(optionalWord.isPresent()).isTrue();
        testWord(optionalWord.get(), id, word, translations);
    }


    private void testWord(Word word, long id, String name, List<String> translations) {
        AssertionsForClassTypes.assertThat(word.getWord()).isEqualTo(name);
        AssertionsForClassTypes.assertThat(word.getId()).isEqualTo(id);
        AssertionsForClassTypes.assertThat(word.getCollection()).isNotNull();
        AssertionsForInterfaceTypes.assertThat(word.getTranslations()).isNotEmpty();
        testTranslationsFetchMode(word.getTranslations(), true);
        AssertionsForClassTypes.assertThat(word.getTranslations().size()).isEqualTo(translations.size());
        List<String> foundedTranslations = word.getTranslations().stream().map(Translation::getName).collect(Collectors.toList());
        AssertionsForInterfaceTypes.assertThat(foundedTranslations).containsAll(translations);
    }

    private void testTranslationsFetchMode(Set<Translation> translations, boolean shouldBeInitialized) {
        AssertionsForClassTypes.assertThat(Hibernate.isInitialized(translations)).isEqualTo(shouldBeInitialized);
    }

    private void testAllTranslationsFetchMode(List<Word> words, boolean isEager) {
        for (Word word : words) {
            testTranslationsFetchMode(word.getTranslations(), isEager);
        }
    }

    @Test
    public void shouldGetByFilter() {
        WordFilter userFilter1 = WordFilter.builder(1L).build();
        testFindByFilter(userFilter1, Arrays.asList("dog", "dog", "car", "mouse"));

        WordFilter userFilter2 = WordFilter.builder(2L).build();
        testFindByFilter(userFilter2, Arrays.asList("duck", "phone", "hound"));

        WordFilter wordFilter = WordFilter.builder(1L).name("dog").build();
        testFindByFilter(wordFilter, Arrays.asList("dog", "dog"));

        WordFilter collectionFilter = WordFilter.builder(1L).collectionId(1L).build();
        testFindByFilter(collectionFilter, Arrays.asList("dog", "dog", "car", "mouse"));

        WordFilter emptyCollectionFilter = WordFilter.builder(1L).collectionId(2L).build();
        testFindByFilter(emptyCollectionFilter, Collections.emptyList());

        WordFilter posFilter = WordFilter.builder(1L).partOfSpeech(PartOfSpeech.PRONOUN).build();
        testFindByFilter(posFilter, Arrays.asList("dog", "dog", "mouse"));
    }

    private void testFindByFilter(WordFilter filter, List<String> expectedWords) {
        List<Word> words = repository.findAll(WordSpecifications.filterWords(filter));
        AssertionsForClassTypes.assertThat(words.size()).isEqualTo(expectedWords.size());
        testAllTranslationsFetchMode(words, true);
    }

    @Test
    public void shouldInsertWithTranslations() {
        // TODO: przemyśleć to jeszcze
        // TODO: zrobić refaktoring

        Word word = createWordWithTranslation();

        Word savedWord = repository.save(word);
        Optional<Word> optionalWord = repository.findById(savedWord.getId());
        assertThat(optionalWord).isPresent();
        Word newWord = optionalWord.get();
        assertThat(newWord.getTranslations()).isNotEmpty();
        assertThat(newWord.getTranslations().size()).isEqualTo(1);
        assertThat(newWord.getTranslations().stream().map(Translation::getName)).contains("Słowo");


//        Word word2 = new Word();
//        word2.setWord("Word2");
//        word2.setCollection(collection);
//        Translation translation = new Translation();
//        translation.setId(1);
//        word2.setTranslations(Collections.singleton(translation));
//
//        Word savedWord2 = repository.save(word2);
//        Optional<Word> optionalWord2 = repository.findById(savedWord2.getId());
//        assertThat(optionalWord2).isPresent();
//        Word newWord2 = optionalWord2.get();
//        assertThat(newWord2.getTranslations()).isNotEmpty();
//        assertThat(newWord2.getTranslations().size()).isEqualTo(1);
//        assertThat(newWord2.getTranslations().stream().map(Translation::getName)).contains("pies");
    }

    private Word createWordWithTranslation() {
        Word word = createWord();
        Translation translation1 = createTranslation("Słowo");
        word.setTranslations(Collections.singleton(translation1));
        return word;
    }

    private Translation createTranslation(String name) {
        Translation translation1 = new Translation();
        translation1.setName(name);
        Language language = new Language(1);
        translation1.setLanguage(language);
        return translation1;
    }

    private Word createWord() {
        Word word = new Word();
        word.setWord("Word");
        WordsCollection collection = new WordsCollection();
        collection.setId(1L);
        word.setCollection(collection);
        return word;
    }


    @Test
    public void shouldNotInsertDuplicateWord() {
        Word word = createExistingWord();
        assertThatThrownBy(() -> repository.save(word)).isInstanceOf(DataIntegrityViolationException.class);
    }

    private Word createExistingWord() {
        Word word = new Word();
        word.setWord("dog");
        word.setDescription("Dog dog");
        word.setPartOfSpeech(PartOfSpeech.PRONOUN);
        WordsCollection collection = new WordsCollection();
        collection.setId(1);
        word.setCollection(collection);
        word.setMeaning(0);
        return word;
    }

    @Test
    public void shouldNotInsertWithNoRequiredData() {
        // word without name
        Word word = new Word();
        WordsCollection collection = new WordsCollection();
        collection.setId(1);
        word.setCollection(collection);

        assertThatThrownBy(() -> repository.save(word)).isInstanceOf(DataIntegrityViolationException.class);

        // word without collection
        Word word2 = new Word();
        word.setWord("dog");

        assertThatThrownBy(() -> repository.save(word2)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void shouldUpdateWithoutTranslations() {
        // TODO: stworzenie słowa wraz z tłumaczeniem
        Optional<Word> optionalWord = repository.findById(1L);
        // TODO: skopiowanie 
        Word word = createWordWithTranslation();
        word.setId(optionalWord.get().getId());
        word.setWord(optionalWord.get().getWord());
        word.setTranslations(null);

        // TODO: czy zrobi aktualizację, czy nie
        Word savedWord = repository.save(word); // TODO: sprawdzić, czy to będzie działać
        Optional<Word> firstWord = repository.findById(1L);
        System.out.println();

    }


}
