package com.parabbits.wordservice.collection.service;

import java.util.Objects;

public class CollectionDTO {

    private String name;
    private String description;
    private String language;


    public CollectionDTO(String name, String description, String language) {
        this.name = name;
        this.description = description;
        this.language = language;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionDTO that = (CollectionDTO) o;
        return name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, language);
    }
}
