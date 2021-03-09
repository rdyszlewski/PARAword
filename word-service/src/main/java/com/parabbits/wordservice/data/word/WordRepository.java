package com.parabbits.wordservice.data.word;

import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.LinkedList;
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

//@NamedQueries({
//        @NamedQuery(name = "findWordById",
//        query = "SELECT w FROM Word w LEFT JOIN FETCH w.translations T WHERE w.id = :id")
//})
//
//@Service
//public class WordRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//
//    public WordRepository(){
//    }
//
//    public Optional<Word> findById(Long id){
//        var query = (TypedQuery) entityManager.createNamedQuery("findWordById");
//        query.setParameter("id", id);
//        return Optional.of((Word)query.getSingleResult());
//    }
//
//    public List<Word> findByCollection(Long collectionId){
//        return new LinkedList<>();
//    }
//}
