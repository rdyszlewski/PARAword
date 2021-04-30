package com.parabbits.wordservice.word.data.common;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class FilterSpecification<Filter, Entity> {

    public Specification<Entity> getSpecification(Filter filter) {
        return (Specification<Entity>) (root, criteriaQuery, criteriaBuilder) -> {
            setupSpecification(filter, root, criteriaBuilder, criteriaQuery);
            return getPredicate(filter, root, criteriaBuilder, criteriaQuery);
        };
    }

    @FunctionalInterface
    protected interface SpecificationCondition {
        boolean condition();
    }

    @FunctionalInterface
    protected interface SpecificationPredicate {
        Predicate toPredicate();
    }

    protected abstract Predicate getPredicate(Filter filter, Root<Entity> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query);

    protected abstract void setupSpecification(Filter filter, Root<Entity> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery);
}
