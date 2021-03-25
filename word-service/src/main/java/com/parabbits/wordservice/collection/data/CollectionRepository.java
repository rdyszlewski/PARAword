package com.parabbits.wordservice.collection.data;

import com.sun.istack.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<WordsCollection, Long> {

    List<WordsCollection> findByUser(Long id);

    List<WordsCollection> findAll(@Nullable Specification<WordsCollection> specification);

    @Query("SELECT count(w) FROM WordsCollection c JOIN c.words w WHERE c.id = :collectionId")
    long countWordsById(long collectionId);
}
