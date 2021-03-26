package com.parabbits.wordservice.data.collection;

import com.parabbits.wordservice.collection.data.CollectionRepository;
import com.parabbits.wordservice.collection.data.LanguageRepository;
import com.parabbits.wordservice.collection.service.CollectionDTO;
import com.parabbits.wordservice.collection.service.CollectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CollectionServiceTest {

    @MockBean
    private LanguageRepository languageRepository;

    @MockBean
    private CollectionRepository collectionRepository;


    private CollectionService service;

    @BeforeEach
    public void initTests() {
        service = new CollectionService(collectionRepository);
    }

    @Test
    public void shouldThrowErrorWhenCreateWithInvalidData() {
        Class<?> exceptionClass = IllegalArgumentException.class;
        // TODO: chcemy sprawdzić, czy wyskoczy błąd
        CollectionDTO invalidNameDTO = new CollectionDTO(0L, null, "Opis", 1, 3, 1, false);
        assertThatThrownBy(() -> service.addCollection(invalidNameDTO)).isInstanceOf(exceptionClass);

        CollectionDTO emptyNameDTO = new CollectionDTO(0L, "", "Opis", 1, 3, 1, false);
        assertThatThrownBy(() -> service.addCollection(emptyNameDTO)).isInstanceOf(exceptionClass);

        CollectionDTO emptyNameDTO2 = new CollectionDTO(0L, " ", "Opis", 1, 3, 1, false);
        assertThatThrownBy(() -> service.addCollection(emptyNameDTO2)).isInstanceOf(exceptionClass);

        CollectionDTO invalidLanguage1DTO = new CollectionDTO(0L, "One", "Description", 0, 3, 1, false);
        assertThatThrownBy(() -> service.addCollection(invalidLanguage1DTO)).isInstanceOf(exceptionClass);

        CollectionDTO invalidLanguage2DTO = new CollectionDTO(0L, "One", "Description", 1, 0, 1, false);
        assertThatThrownBy(() -> service.addCollection(invalidLanguage2DTO)).isInstanceOf(exceptionClass);

        CollectionDTO invalidUserDTO = new CollectionDTO(0L, "One", "Description", 1, 3, 0, false);
        assertThatThrownBy(() -> service.addCollection(invalidUserDTO)).isInstanceOf(exceptionClass);

    }

    @Test
    public void shouldThrowExceptionWhenIdAlreadyExists() {
        Class<?> exceptionClass = IllegalArgumentException.class;
        CollectionDTO invalidUserDTO = new CollectionDTO(2L, "New", "Opis", 1, 3, 1, false);
        when(collectionRepository.existsById(2L)).thenReturn(true);
        assertThatThrownBy(() -> service.addCollection(invalidUserDTO)).isInstanceOf(exceptionClass);
    }
}
