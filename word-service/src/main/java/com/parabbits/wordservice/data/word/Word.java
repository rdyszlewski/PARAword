package com.parabbits.wordservice.data.word;

import com.parabbits.wordservice.collection.data.WordsCollection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@NamedQueries({
        @NamedQuery(
                name = "findWordWithTranslations",
                query = "select e from Word e INNER JOIN FETCH e.translations t where e.id = :id"
        )}
)

@Entity
@Table(name = "words")
@Data
@EqualsAndHashCode(exclude = {"translations"})
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String word;

    private String description;

    @Column(columnDefinition = "int default 0")
    private int meaning;

    @Enumerated(value = EnumType.ORDINAL)
    private PartOfSpeech partOfSpeech;

    @ManyToOne(targetEntity = WordsCollection.class)
    private WordsCollection collection;

    @ManyToMany(targetEntity = Translation.class, fetch = FetchType.LAZY)
    @JoinTable(name = "words_translations",
            joinColumns = {@JoinColumn(name = "word_fk")},
            inverseJoinColumns = {@JoinColumn(name = "translation_fk")})
    private Set<Translation> translations;

}
