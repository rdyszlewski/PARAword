package com.parabbits.wordservice.data.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CollectionRepository extends JpaRepository<WordsCollection, Long> {
    List<WordsCollection> findByUser(Long id);
}
