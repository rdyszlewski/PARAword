package com.parabbits.wordservice.word.data.word;

import com.parabbits.wordservice.collection.data.WordsCollection;
import com.parabbits.wordservice.word.data.common.PartOfSpeech;
import com.parabbits.wordservice.word.data.translation.Translation;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NamedQueries({
        @NamedQuery(
                name = "findWordWithTranslations",
                query = "select e from Word e INNER JOIN FETCH e.translations t where e.id = :id"
        )}
)

@Entity
@Table(name = "words", uniqueConstraints = @UniqueConstraint(columnNames = {"word", "meaning", "partOfSpeech", "collection_id"}))
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"translations"})
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull
    private String word;

    private String description;

    @Column(columnDefinition = "int default 0")
    private int meaning;

    @Enumerated(value = EnumType.ORDINAL)
    private PartOfSpeech partOfSpeech;

    @ManyToOne(targetEntity = WordsCollection.class)
    @NotNull
    private WordsCollection collection;

    @ManyToMany(targetEntity = Translation.class, fetch = FetchType.LAZY)
    @JoinTable(name = "words_translations",
            joinColumns = {@JoinColumn(name = "word_fk")},
            inverseJoinColumns = {@JoinColumn(name = "translation_fk")})
    private Set<Translation> translations;

}
