package com.parabbits.wordservice.collection.data;

import com.parabbits.wordservice.collection.service.CollectionAccess;
import com.sun.istack.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<WordsCollection, Long> {

    List<WordsCollection> findByUser(Long id);

    List<WordsCollection> findAll(@Nullable Specification<WordsCollection> specification);

    @Query("SELECT count(w) FROM WordsCollection c JOIN c.words w WHERE c.id = :collectionId")
    long countWordsById(long collectionId);

    @Query("SELECT c.user FROM WordsCollection c WHERE c.id = :collectionId")
    Long findUserId(long collectionId);

    @Query("SELECT new com.parabbits.wordservice.collection.service.CollectionAccess(isPublic, c.user) from WordsCollection c where c.id=:collectionId")
    Optional<CollectionAccess> findCollectionAccess(Long collectionId);
}
