package com.parabbits.wordservice.data.word;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

public class WordSpecifications {

    public static Specification<Word> filterWords(WordFilter filter){
        return new Specification<Word>() {
            @Override
            public Predicate toPredicate(Root<Word> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                root.fetch("translations", JoinType.LEFT);
                criteriaQuery.distinct(true);
                List<Predicate> predicateList = new LinkedList<>();
                if(filter.getName() != null){
                    Predicate predicate  = criteriaBuilder.like(root.get("word"), filter.getName());
                    predicateList.add(predicate);
                }
                if(filter.getCollectionId() > 0){
                    Predicate predicate = criteriaBuilder.equal(root.get("collection").get("id"), filter.getCollectionId());
                    predicateList.add(predicate);
                }
                if(filter.getUserId() > 0){
                    Predicate predicate = criteriaBuilder.equal(root.get("collection").get("user"), filter.getUserId());
                    predicateList.add(predicate);
                }
                if(filter.getPartOfSpeech() != null){
                    Predicate predicate = criteriaBuilder.equal(root.get("partOfSpeech"), filter.getPartOfSpeech());
                    predicateList.add(predicate);
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate [0]));
            }
        };
        // TODO: może sprawdzić, czy da się ustawić tutaj jakoś limit
    }
}
