package com.parabbits.wordservice.data.word;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

public abstract class FilterSpecification<T> {

    @FunctionalInterface
    protected interface SpecificationCondition{
        boolean condition();
    }

    @FunctionalInterface
    protected interface SpecificationPredicate{
        Predicate toPredicate();
    }

    protected abstract Map<SpecificationCondition, SpecificationPredicate> getSpecificationMap(T filter, Root root, CriteriaBuilder criteriaBuilder, CriteriaQuery query);
}
