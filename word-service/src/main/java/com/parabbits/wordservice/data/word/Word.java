package com.parabbits.wordservice.data.word;

import com.parabbits.wordservice.data.collection.WordsCollection;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@NamedQueries({
    @NamedQuery(
            name = "findWordWithTranslations",
            query = "select e from Word e INNER JOIN FETCH e.translations t where e.id = :id"
    )}
)

@Entity
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String word;

    private String description;

    @Enumerated(value = EnumType.ORDINAL)
    private PartOfSpeech partOfSpeech;

    @OneToOne(targetEntity = WordsCollection.class)
    private WordsCollection collection;

    @ManyToMany(targetEntity = Translation.class, fetch = FetchType.LAZY)
    @JoinTable(name = "words_translations",
        joinColumns = {@JoinColumn(name = "word_fk")},
        inverseJoinColumns = {@JoinColumn(name = "translation_fk")})
    private Set<Translation> translations;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public WordsCollection getCollection() {
        return collection;
    }

    public void setCollection(WordsCollection collection) {
        this.collection = collection;
    }

    public Set<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<Translation> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return id == word1.id &&
                word.equals(word1.word) &&
                Objects.equals(description, word1.description) &&
                partOfSpeech == word1.partOfSpeech &&
                collection.equals(word1.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, description, partOfSpeech, collection);
    }
}
