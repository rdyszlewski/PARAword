package com.parabbits.wordservice.data.word;

import com.parabbits.wordservice.data.collection.Language;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
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
