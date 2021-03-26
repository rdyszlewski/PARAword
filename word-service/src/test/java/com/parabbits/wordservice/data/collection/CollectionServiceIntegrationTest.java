package com.parabbits.wordservice.data.collection;


import com.parabbits.wordservice.collection.data.CollectionFilter;
import com.parabbits.wordservice.collection.service.CollectionDTO;
import com.parabbits.wordservice.collection.service.CollectionResponseDTO;
import com.parabbits.wordservice.collection.service.CollectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:initialization_db.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup_db.sql")
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CollectionServiceIntegrationTest {

    @Autowired
    private CollectionService service;

    @Test
    public void shouldReturnEntireCollection() {
        CollectionResponseDTO dto = service.getById(1L);
        assertThat(dto.getName()).isEqualTo("Angielski 1");
        assertThat(dto.getWordsCount()).isEqualTo(4);

    }

    @Test
    public void shouldReturnByUser() {
        List<CollectionResponseDTO> collections = service.getByUser(2L);
        assertThat(collections.size()).isEqualTo(2);
        assertThat(collections.get(0).getWordsCount()).isEqualTo(3);
        assertThat(collections.get(1).getWordsCount()).isEqualTo(0);
    }

    @Test
    public void shouldReturnByFilter() {
        CollectionFilter publicFilter = CollectionFilter.builder().name("Angielski").build();
        List<CollectionResponseDTO> publicCollections = service.getByFilter(publicFilter);
        assertThat(publicCollections.size()).isEqualTo(1);

        CollectionFilter userFilter = CollectionFilter.builder().name("Angielski").userId(1L).build();
        List<CollectionResponseDTO> userCollections = service.getByFilter(userFilter);
        assertThat(userCollections.size()).isEqualTo(2);
        List<String> collectionsNames = userCollections.stream().map(CollectionResponseDTO::getName).collect(Collectors.toList());
        assertThat(collectionsNames).containsAll(Arrays.asList("Angielski 1", "Angielski 3"));
    }

    @Test
    public void shouldSaveCollection() {
        CollectionDTO dto = new CollectionDTO(-1L, "New", "Collection desc", 1, 3, 1, false);
        CollectionResponseDTO responseDTO = service.addCollection(dto);
        assertThat(responseDTO.getName()).isEqualTo("New");
//        assertThat(responseDTO.getLanguage1()).isEqualTo("eng");
//        assertThat(responseDTO.getLanguage2()).isEqualTo("pl");
        assertThat(responseDTO.getWordsCount()).isEqualTo(0);

        CollectionResponseDTO savedCollection = service.getById(responseDTO.getId());
        assertThat(savedCollection.getId()).isGreaterThan(0);

        CollectionDTO collectionDTO2 = new CollectionDTO(0L, "New", "Collection desc", 1, 3, 1, false);
        CollectionResponseDTO responseDTO2 = service.addCollection(collectionDTO2);
        assertThat(responseDTO2.getId()).isNotEqualTo(0L);

        CollectionDTO collectionDTO3 = new CollectionDTO(-1L, "New", "Collection desc", 1, 3, 1, false);
        CollectionResponseDTO responseDTO3 = service.addCollection(collectionDTO3);
        assertThat(responseDTO3.getId()).isNotEqualTo(-1L);

        CollectionDTO collectionDTO4 = new CollectionDTO(2000L, "New", " ", 1, 3, 1, false);
        CollectionResponseDTO responseDTO4 = service.addCollection(collectionDTO4);
        assertThat(responseDTO4.getId()).isNotEqualTo(2000L);
    }

    @Test
    public void shouldThrowErrorWhenInvalidData() {
        Class<?> existsIdException = IllegalArgumentException.class;
        CollectionDTO invalidIdDTO = new CollectionDTO(2L, "New", null, 1, 3, 1, false);
        assertThatThrownBy(() -> service.addCollection(invalidIdDTO)).isInstanceOf(existsIdException);
    }
    
}
