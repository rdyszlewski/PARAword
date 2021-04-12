package com.parabbits.wordservice.data.word;

import com.parabbits.wordservice.collection.data.WordsCollection;

import javax.persistence.criteria.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class TranslationFilterSpecification extends FilterSpecification<TranslationFilter, Translation> {

    @Override
    protected void setupSpecification(TranslationFilter translationFilter, Root<Translation> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery) {
        criteriaQuery.distinct(true);
    }

    @Override
    protected Predicate getPredicate(TranslationFilter filter, Root<Translation> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query) {
        Map<SpecificationCondition, SpecificationPredicate> predicateMap = new HashMap<>() {{
            put(() -> filter.getName() != null,
                    () -> criteriaBuilder.like(root.get("name"), filter.getName()));
            put(() -> filter.getUserId() > 0,
                    () -> criteriaBuilder.equal(root.get("user"), filter.getUserId()));
            put(() -> filter.getLanguageId() > 0,
                    () -> criteriaBuilder.equal(root.get("language").get("id"), filter.getLanguageId()));
            put(() -> filter.getDescription() != null,
                    () -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), MessageFormat.format("%{0}%", filter.getDescription().toLowerCase())));
            put(() -> filter.getPartOfSpeech() != null,
                    () -> criteriaBuilder.equal(root.get("partOfSpeech"), filter.getPartOfSpeech()));
            put(() -> filter.getMeaning() >= 0,
                    () -> criteriaBuilder.equal(root.get("meaning"), filter.getMeaning()));
            put(() -> filter.getWord() != null, () -> {
                Join<Word, Translation> wordJoin = root.join("words");
                return criteriaBuilder.equal(wordJoin.get("word"), filter.getWord());
            });
            put(() -> filter.getCollectionId() > 0, () -> {
                Join<Word, Translation> wordJoin = root.join("words");
                Join<WordsCollection, Translation> collectionJoin = wordJoin.join("collection");
                return criteriaBuilder.equal(collectionJoin.get("id"), filter.getCollectionId());
            });
        }};
        return criteriaBuilder.and(predicateMap.entrySet().stream().filter(entry -> entry.getKey().condition()).map(entry -> entry.getValue().toPredicate()).toArray(Predicate[]::new));
    }
}
