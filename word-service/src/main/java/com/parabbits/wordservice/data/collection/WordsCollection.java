package com.parabbits.wordservice.data.collection;

import com.parabbits.wordservice.data.word.Word;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@NamedQueries({
        @NamedQuery(
                name = "selectUserIdFromCollection",
                query = "select w.user.id from WordsCollection w where id = :id"
        )
})

@Entity
@Table(name = "collections")
@Data
public class WordsCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language1")
    private Language learningLanguage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language2")
    private Language nativeLanguage;

    @Column(columnDefinition = "int check(user > 0)")
    private long user;
    // TODO: zastanowić się, czy ten user będzie tutaj ok

    @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<Word> words;

    @Column(name = "is_public")
    private Boolean isPublic;

    public void setLearningLanguage(Language learningLanguage) {
        if (learningLanguage != null && learningLanguage.getId() > 0) {
            this.learningLanguage = learningLanguage;
        }
    }
}
