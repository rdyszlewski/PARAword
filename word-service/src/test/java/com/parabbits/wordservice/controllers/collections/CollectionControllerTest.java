package com.parabbits.wordservice.controllers.collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parabbits.wordservice.collection.data.CollectionFilter;
import com.parabbits.wordservice.collection.service.CollectionResponseDTO;
import com.parabbits.wordservice.collection.service.CollectionService;
import com.parabbits.wordservice.collection.service.LanguageDTO;
import com.parabbits.wordservice.security.UserPrincipal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CollectionControllerTest {

    private final String COLLECTION_PATH = "/collection";
    private final String GET_BY_ID_PATH = COLLECTION_PATH + "/1";

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private CollectionService service;

    @BeforeAll
    public static void init() {
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);

    }

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        // TODO: tutaj można dodać filtry
        CollectionController controller = new CollectionController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller)
//                .apply(springSecurity(new CollectionTestFilter()))
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldGetById() throws Exception {
        CollectionResponseDTO dto = getResponseDTO();
        UserPrincipal principal = new UserPrincipal(1L, "Zbyszek");
        when(service.getById(1L)).thenReturn(dto);
        MockHttpServletResponse response = performMVC(GET_BY_ID_PATH, principal);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(dto));

        when(service.getById(1L)).thenReturn(null);
        MockHttpServletResponse response2 = performMVC(GET_BY_ID_PATH, principal);
        assertThat(response2.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.getContentAsString()).isEmpty();
    }

    @Test
    public void shouldBlockGetCollectionByIdWhenIncorrectUser() throws Exception {
        CollectionResponseDTO dto = getResponseDTO(); // userId = 1, public = false
        when(service.getById(1L)).thenReturn(dto);
        MockHttpServletResponse response = performMVC(GET_BY_ID_PATH, new UserPrincipal(2L, "Heniu"));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void shouldGetPublicCollection() throws Exception {
        CollectionResponseDTO publicDTO = getPublicCollectionDTO();
        when(service.getById(1L)).thenReturn(publicDTO);
        UserPrincipal ownerUser = new UserPrincipal(1L, "Zbyszek");
        MockHttpServletResponse response = performMVC(GET_BY_ID_PATH, ownerUser);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(publicDTO));

        UserPrincipal notOwnerUser = new UserPrincipal(2L, "Heniu");
        MockHttpServletResponse response2 = performMVC(GET_BY_ID_PATH, notOwnerUser);
        assertThat(response2.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(publicDTO));
    }

    private MockHttpServletResponse performMVC(String path) throws Exception {
        return performMVC(path, null);

    }

    private MockHttpServletResponse performMVC(String path, UserPrincipal principal) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(path).contentType(MediaType.APPLICATION_JSON);
        if (principal != null) {
            builder.principal(principal);
        }
        return mvc.perform(builder).andReturn().getResponse();
    }

    private CollectionResponseDTO getResponseDTO() {
        LanguageDTO language1 = new LanguageDTO(1L, "eng");
        LanguageDTO language2 = new LanguageDTO(3L, "pl");
        return new CollectionResponseDTO(1L, "Name", "Description", language1, language2, "1", 10, false);
    }

    private CollectionResponseDTO getPublicCollectionDTO() {
        LanguageDTO language1 = new LanguageDTO(1L, "eng");
        LanguageDTO language2 = new LanguageDTO(3L, "pl");
        return new CollectionResponseDTO(1L, "Name", "Description", language1, language2, "1", 10, true);
    }

    @Test
    public void shouldReturnCollectionsByFilter() throws Exception {
        String namePath = COLLECTION_PATH + "?name=kaszanka";
        CollectionFilter nameFilter = CollectionFilter.builder().name("kaszanka").build();
        testGetByFilter(namePath, nameFilter);

        String language1Path = COLLECTION_PATH + "?language1=1";
        CollectionFilter language1Filter = CollectionFilter.builder().language1(1L).build();
        testGetByFilter(language1Path, language1Filter);

        String userPath = COLLECTION_PATH + "?userId=1";
        CollectionFilter userFilter = CollectionFilter.builder().userId(1L).build();
        testGetByFilter(userPath, userFilter);

        String manyPath = COLLECTION_PATH + "?name=kaszanka&userId=1";
        CollectionFilter manyFilter = CollectionFilter.builder().name("kaszanka").userId(1L).build();
        testGetByFilter(manyPath, manyFilter);

    }

    private void testGetByFilter(String path, CollectionFilter filter) throws Exception {
        when(service.getByFilter(filter)).thenReturn(Collections.singletonList(getResponseDTO()));
        MockHttpServletResponse response = performMVC(path);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(Arrays.asList(getResponseDTO())));
    }

    @Test
    public void shouldBlockGetCollectionWhenIncorrectUser() throws Exception {
        // TODO: dokończyć
    }
}
