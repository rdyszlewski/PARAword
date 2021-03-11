package com.parabbits.wordservice.data.word;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    @EntityGraph(attributePaths = {"words"})
    Optional<Translation> findById(Long id);

    @Query(value = "SELECT count(t)>0 FROM Translation t LEFT JOIN t.words w WHERE w.id = :wordId AND t.name = :translation")
    boolean existsByWordAndName(long wordId, String translation);

    @Query(value = "SELECT t FROM Translation t LEFT JOIN t.words w WHERE w.id = :wordId")
    List<Translation> findByWord(long wordId);

    List<Translation> findAll(Specification<Translation> specification);
}
