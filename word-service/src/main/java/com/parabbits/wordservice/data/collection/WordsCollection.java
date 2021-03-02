package com.parabbits.wordservice.data.collection;

import com.parabbits.wordservice.data.collection.Language;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "collections")
public class WordsCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToOne(targetEntity = Language.class)
    private Language language;

    @Column(nullable = false)
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
        this.language = language;
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
