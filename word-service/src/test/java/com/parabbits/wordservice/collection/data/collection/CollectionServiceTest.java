package com.parabbits.wordservice.collection.data.collection;

import com.parabbits.wordservice.collection.data.CollectionRepository;
import com.parabbits.wordservice.collection.data.LanguageRepository;
import com.parabbits.wordservice.collection.service.CollectionAccess;
import com.parabbits.wordservice.collection.service.CollectionDTO;
import com.parabbits.wordservice.collection.service.CollectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CollectionServiceTest {

    @Test
    public void shouldThrowErrorWhenCreateWithInvalidData() {
        Class<?> exceptionClass = IllegalArgumentException.class;
        validateData(exceptionClass, (dto, service) -> service.addCollection(dto));
    }

    @MockBean
    private LanguageRepository languageRepository;

    @MockBean
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionService service;

//    @BeforeEach
//    public void initTests() {
//        service = new CollectionService(collectionRepository);
//    }

    private void validateData(Class<?> exceptionClass, ServiceExecutor executor) {
        CollectionDTO invalidNameDTO = new CollectionDTO(0L, null, "Opis", 1, 3, 1, false);
        assertThatThrownBy(() -> executor.execute(invalidNameDTO, service)).isInstanceOf(exceptionClass);

        CollectionDTO emptyNameDTO = new CollectionDTO(0L, "", "Opis", 1, 3, 1, false);
        assertThatThrownBy(() -> executor.execute(emptyNameDTO, service)).isInstanceOf(exceptionClass);

        CollectionDTO emptyNameDTO2 = new CollectionDTO(0L, " ", "Opis", 1, 3, 1, false);
        assertThatThrownBy(() -> executor.execute(emptyNameDTO2, service)).isInstanceOf(exceptionClass);

        CollectionDTO invalidLanguage1DTO = new CollectionDTO(0L, "One", "Description", 0, 3, 1, false);
        assertThatThrownBy(() -> executor.execute(invalidLanguage1DTO, service)).isInstanceOf(exceptionClass);

        CollectionDTO invalidLanguage2DTO = new CollectionDTO(0L, "One", "Description", 1, 0, 1, false);
        assertThatThrownBy(() -> executor.execute(invalidLanguage2DTO, service)).isInstanceOf(exceptionClass);

        CollectionDTO invalidUserDTO = new CollectionDTO(0L, "One", "Description", 1, 3, 0, false);
        assertThatThrownBy(() -> executor.execute(invalidUserDTO, service)).isInstanceOf(exceptionClass);
    }

    @Test
    public void shouldThrowExceptionWhenUpdateWithIncorrectData() {
        Class<?> exceptionClass = IllegalArgumentException.class;
        validateData(exceptionClass, (dto, service) -> service.updateCollection(dto));
    }

    @Test
    public void shouldThrowExceptionWhenIdAlreadyExists() {
        Class<?> exceptionClass = IllegalArgumentException.class;
        CollectionDTO invalidUserDTO = new CollectionDTO(2L, "New", "Opis", 1, 3, 1, false);
        when(collectionRepository.existsById(2L)).thenReturn(true);
        assertThatThrownBy(() -> service.addCollection(invalidUserDTO)).isInstanceOf(exceptionClass);
    }

    @Test
    public void shouldReturnCollectionAccess() {
        when(collectionRepository.findCollectionAccess(1L)).thenReturn(Optional.of(CollectionAccess.getAccess(false, 1L)));
        CollectionAccess result1 = service.getCollectionAccess(1L);
        assertThat(result1.exist()).isTrue();
        assertThat(result1.isOwner(1L)).isTrue();

        when(collectionRepository.findCollectionAccess(1L)).thenReturn(Optional.empty());
        CollectionAccess result2 = service.getCollectionAccess(1L);
        assertThat(result2).isNotNull();
        assertThat(result2.exist()).isFalse();
        assertThat(result2.isOwner(1L)).isFalse();
        assertThat(result2.canAccess(1L)).isFalse();


    }

    @FunctionalInterface
    private interface ServiceExecutor {
        void execute(CollectionDTO dto, CollectionService service);
    }

}
