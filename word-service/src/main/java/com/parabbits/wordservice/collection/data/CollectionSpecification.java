package com.parabbits.wordservice.collection.data;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class CollectionSpecification {

    public static Specification<WordsCollection> getSpecification(CollectionFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            List<Predicate> predicateList = new LinkedList<>();
            if (filter.getName() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), MessageFormat.format("%{0}%", filter.getName().toLowerCase()));
                predicateList.add(predicate);
            }
            if (filter.getUserId() > 0) {
                Predicate predicate = criteriaBuilder.equal(root.get("user"), filter.getUserId());
                predicateList.add(predicate);
            }

            if (filter.getDescription() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), MessageFormat.format("%{0}%", filter.getDescription()));
                predicateList.add(predicate);
            }
            if (filter.getLanguage1() > 0) {
                Predicate predicate = criteriaBuilder.equal(root.get("learningLanguage").get("id"), filter.getLanguage1());
                predicateList.add(predicate);
            }
            if (filter.getLanguage2() > 0) {
                Predicate predicate = criteriaBuilder.equal(root.get("nativeLanguage").get("id"), filter.getLanguage2());
                predicateList.add(predicate);
            }
            if (filter.isPublicCollection() || filter.getUserId() <= 0) {
                Predicate predicate = criteriaBuilder.equal(root.get("isPublic"), true);
                predicateList.add(predicate);
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
