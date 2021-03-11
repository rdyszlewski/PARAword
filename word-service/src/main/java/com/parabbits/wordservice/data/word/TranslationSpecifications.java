package com.parabbits.wordservice.data.word;

import com.parabbits.wordservice.data.collection.WordsCollection;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TranslationSpecifications extends FilterSpecification<TranslationFilter>{


    @Override
    protected  Map<SpecificationCondition, SpecificationPredicate> getSpecificationMap(TranslationFilter filter, Root root, CriteriaBuilder criteriaBuilder, CriteriaQuery query) {
        Map<SpecificationCondition, SpecificationPredicate> predicateMap = new HashMap<>(){{
            put(()->filter.getName() != null, ()->criteriaBuilder.like(root.get("name"), filter.getName()));
            put(()->filter.getUserId() > 0, ()->criteriaBuilder.equal(root.get("user"), filter.getUserId()));
            put(()->filter.getLanguageId() > 0, ()->criteriaBuilder.equal(root.get("language").get("id"), filter.getLanguageId()));
            put(()->filter.getDescription() != null, ()->criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%"));
            put(()->filter.getPartOfSpeech() != null, ()->criteriaBuilder.equal(root.get("partOfSpeech"), filter.getPartOfSpeech()));
            put(()->filter.getMeaning() >= 0, ()->criteriaBuilder.equal(root.get("meaning"), filter.getMeaning()));
            put(()->filter.getWord() != null, ()->{
                Join<Word, Translation> wordJoin = root.join("words");
                return criteriaBuilder.equal(wordJoin.get("word"), filter.getWord());
            });
            put(()->filter.getCollectionId() > 0, ()->{
                Join<Word, Translation> wordJoin = root.join("words");
                Join<WordsCollection, Translation> collectionJoin = wordJoin.join("collection");
                return criteriaBuilder.equal(collectionJoin.get("id"), filter.getCollectionId());
            });
        }};
        return predicateMap;
    }



    public Specification<Translation> getFilterSpecification(TranslationFilter filter){
        return (Specification<Translation>) (root, criteriaQuery, criteriaBuilder) -> {
            Map<SpecificationCondition, SpecificationPredicate> predicateMap = getSpecificationMap(filter, root, criteriaBuilder, criteriaQuery);
            List<Predicate> predicateList = predicateMap.entrySet().stream().filter(entry -> entry.getKey().condition()).map(entry->entry.getValue().toPredicate()).collect(Collectors.toList());
            criteriaQuery.distinct(true);
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
