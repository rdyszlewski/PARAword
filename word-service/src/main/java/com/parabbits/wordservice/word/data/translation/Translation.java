package com.parabbits.wordservice.word.data.translation;

import com.parabbits.wordservice.collection.data.Language;
import com.parabbits.wordservice.word.data.common.PartOfSpeech;
import com.parabbits.wordservice.word.data.word.Word;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "translations")
@Data
@EqualsAndHashCode
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(columnDefinition = "int default 0")
    private int meaning;

    @Enumerated(value = EnumType.ORDINAL)
    private PartOfSpeech partOfSpeech;

    @ManyToMany(mappedBy = "translations", fetch = FetchType.LAZY)
    private Set<Word> words;

    private long user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id")
    private Language language;
}
