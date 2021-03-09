package com.parabbits.wordservice.data.word;

import com.sun.istack.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @EntityGraph(attributePaths = {"translations"})
    Optional<Word> findById(Long id);

    @Query(value = "SELECT w FROM Word w LEFT JOIN FETCH w.collection c WHERE c.id = :id")
    List<Word> findByCollection(Long id);

    // TODO: można dodać wariant z Pageable i Sort
    List<Word> findAll(@Nullable Specification<Word> specification);
}

