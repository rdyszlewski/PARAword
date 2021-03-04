package com.parabbits.wordservice.data.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Repository
public class WordRepository  {


    private EntityManager entityManager;

    @Autowired
    public WordRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Optional<Word> findById(long id){
        return Optional.of(entityManager.find(Word.class, id));
    }

    public Optional<Word> findByIdWithTranslations(long id){
        TypedQuery query = (TypedQuery) entityManager.createNamedQuery("findWordWithTranslations");
        query.setParameter("id", id);

        Word word = (Word) query.getSingleResult();
        return Optional.of(word);
    }

    public Optional<Word> save(Word word){
        return Optional.of(entityManager.merge(word));
    }


}
