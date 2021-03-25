package com.parabbits.wordservice.data.collection;


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

import static org.assertj.core.api.Assertions.assertThat;

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

//    @MockBean
//    private LanguageRepository languageRepository;
//
//    @MockBean
//    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionService service;

    @Test
    public void shouldReturnEntireCollection() {
        CollectionResponseDTO dto = service.getById(1L);
        assertThat(dto.getName()).isEqualTo("Angielski 1");
        assertThat(dto.getWordsCount()).isEqualTo(4);

    }
}
