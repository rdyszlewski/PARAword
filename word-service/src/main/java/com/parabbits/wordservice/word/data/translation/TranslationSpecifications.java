package com.parabbits.wordservice.word.data.translation;

import org.springframework.data.jpa.domain.Specification;

public class TranslationSpecifications {

    private final static TranslationFilterSpecification filterSpecification = new TranslationFilterSpecification();

    public static Specification<Translation> getFilterSpecification(TranslationFilter filter) {
        return filterSpecification.getSpecification(filter);
    }
}
