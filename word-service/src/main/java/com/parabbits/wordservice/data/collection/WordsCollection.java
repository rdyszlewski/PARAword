package com.parabbits.wordservice.data.collection;

import com.parabbits.wordservice.data.collection.Language;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(
                name = "selectUserIdFromCollection",
                query = "select w.user.id from WordsCollection w where id = :id"
        )
})

@Entity
@Table(name = "collections")
public class WordsCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(columnDefinition = "int check(user > 0)")
    private long user;
    // TODO: zastanowić się, czy ten user będzie tutaj ok


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        if(language != null && language.getId() > 0){
            this.language = language;
        }
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordsCollection that = (WordsCollection) o;
        return id == that.id &&
                user == that.user &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, language, user);
    }
}
