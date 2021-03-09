package com.parabbits.wordservice.data.word;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    @EntityGraph(attributePaths = {"translations"})
    Optional<Translation> findById(Long id);

    // TODO: dodać kiedyś findByCollection

    // TODO: przemyśleć jak to powinno być

//    @Query(value = "SELECT count(t)>0 FROM Translation t WHERE t.words.id = wordId")
//    boolean exists(long wordId, String translation);
}
