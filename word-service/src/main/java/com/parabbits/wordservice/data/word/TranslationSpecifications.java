package com.parabbits.wordservice.data.word;

import org.springframework.data.jpa.domain.Specification;

public class TranslationSpecifications{

    private final static TranslationFilterSpecification filterSpecification = new TranslationFilterSpecification();

    public static Specification<Translation> getFilterSpecification(TranslationFilter filter){
        return filterSpecification.getSpecification(filter);
    }
}
