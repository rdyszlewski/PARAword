package com.parabbits.wordservice.controllers.collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parabbits.wordservice.collection.data.CollectionFilter;
import com.parabbits.wordservice.collection.service.*;
import com.parabbits.wordservice.controllers.advice.CommonAdvice;
import com.parabbits.wordservice.security.UserPrincipal;
import com.parabbits.wordservice.utils.MockMvcHelper;
import com.parabbits.wordservice.utils.RestMethod;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CollectionControllerTest {

    private final String COLLECTION_PATH = "/collection";
    private final String GET_BY_ID_PATH = COLLECTION_PATH + "/1";

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private CollectionService service;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        // TODO: tutaj można dodać filtry
        CollectionController controller = new CollectionController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller)
//                .apply(springSecurity(new CollectionTestFilter()))
                .setControllerAdvice(new CommonAdvice())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldGetById() throws Exception {
        CollectionResponseDTO dto = getResponseDTO();
        UserPrincipal principal = new UserPrincipal(1L, "Zbyszek");
        when(service.getById(1L)).thenReturn(dto);
        MockHttpServletResponse response = performGetMVC(GET_BY_ID_PATH, principal);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(dto));

        when(service.getById(1L)).thenReturn(null);
        MockHttpServletResponse response2 = performGetMVC(GET_BY_ID_PATH, principal);
        assertThat(response2.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldBlockGetCollectionByIdWhenIncorrectUser() throws Exception {
        CollectionResponseDTO dto = getResponseDTO(); // userId = 1, public = false
        when(service.getById(1L)).thenReturn(dto);
        MockHttpServletResponse response = performGetMVC(GET_BY_ID_PATH, new UserPrincipal(2L, "Heniu"));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void shouldGetPublicCollection() throws Exception {
        CollectionResponseDTO publicDTO = getPublicCollectionDTO();
        when(service.getById(1L)).thenReturn(publicDTO);
        UserPrincipal ownerUser = new UserPrincipal(1L, "Zbyszek");
        MockHttpServletResponse response = performGetMVC(GET_BY_ID_PATH, ownerUser);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(publicDTO));

        UserPrincipal notOwnerUser = new UserPrincipal(2L, "Heniu");
        MockHttpServletResponse response2 = performGetMVC(GET_BY_ID_PATH, notOwnerUser);
        assertThat(response2.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(publicDTO));
    }

    private MockHttpServletResponse performGetMVC(String path, UserPrincipal principal) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(path).contentType(MediaType.APPLICATION_JSON);
        if (principal != null) {
            builder.principal(principal);
        }
        return mvc.perform(builder).andReturn().getResponse();
    }

    private MockHttpServletResponse performPostMVC(String path, CollectionDTO dto, UserPrincipal principal) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(path)
                .content(objectMapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON);
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
        MockHttpServletResponse response = performGetMVC(path, new UserPrincipal(1L, "Heniu"));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(Arrays.asList(getResponseDTO())));
    }

    @Test
    public void shouldBlockGetCollectionWhenIncorrectUser() throws Exception {
        // TODO: Stworzenie filtra, który pobiera prywatne elementy użytkownika
        String path = COLLECTION_PATH + "?userId=1&isPublic=false";
        CollectionFilter filter = CollectionFilter.builder().userId(1L).publicCollection(true).build();

        MockHttpServletResponse response = performGetMVC(path, new UserPrincipal(2L, "Zdzichu"));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void shouldAddNewCollection() throws Exception {
        long userId = 1;
        CollectionDTO dto = new CollectionDTO(null, "One", null, 1, 3, userId, false);
        MockHttpServletResponse response = performPostMVC(COLLECTION_PATH, dto, new UserPrincipal(userId, "Heniu"));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void shouldThrowErrorWhenAddNewCollectionWithIncorrectData() throws Exception {
        long userId = 1;
        CollectionDTO dto = new CollectionDTO(null, "One", null, 1, 200, userId, false);
        when(service.addCollection(dto)).thenThrow(new IllegalArgumentException());
        MockHttpServletResponse response = performPostMVC(COLLECTION_PATH, dto, new UserPrincipal(userId, "Heniu"));
        // TODO: przemysleć, co tutaj powinno być
        assertThat(response.getStatus()).isEqualTo(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void shouldBlockAddNewCollectionWithIncorrectUser() throws Exception {
        long userId = 1;
        CollectionDTO dto = new CollectionDTO(null, "One", null, 1, 200, userId, false);
        MockHttpServletResponse response = performPostMVC(COLLECTION_PATH, dto, new UserPrincipal(2L, "Zbyszek"));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void shouldRemoveCollection() throws Exception {
        String path = COLLECTION_PATH + "/1";
        when(service.getCollectionAccess(1L)).thenReturn(new CollectionAccess(false, 1L));
        testControllerMethod(path, RestMethod.DELETE, getCorrectUser(), HttpStatus.OK);
        testControllerMethod(path, RestMethod.DELETE, getIncorrectUser(), HttpStatus.UNAUTHORIZED);
        testControllerMethod(path, RestMethod.DELETE, null, HttpStatus.UNAUTHORIZED);

        String path2 = COLLECTION_PATH + "/200";
        when(service.getCollectionAccess(200L)).thenReturn(new CollectionAccess(null, null));
        testControllerMethod(path2, RestMethod.DELETE, getCorrectUser(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldUpdateCollection() throws Exception {
        final String path = COLLECTION_PATH + "/1";
        final String path2 = COLLECTION_PATH + "/200";
        when(service.getCollectionAccess(1L)).thenReturn(new CollectionAccess(false, 1L));
        when(service.getCollectionAccess(200L)).thenReturn(new CollectionAccess(null, null));
        CollectionDTO collectionDTO = new CollectionDTO(1L, "One", null, 1L, 2L, 1L, true);
        testControllerMethod(path, RestMethod.PUT, collectionDTO, getCorrectUser(), HttpStatus.OK);
        testControllerMethod(path, RestMethod.PUT, collectionDTO, null, HttpStatus.UNAUTHORIZED);
        testControllerMethod(path, RestMethod.PUT, collectionDTO, getIncorrectUser(), HttpStatus.UNAUTHORIZED);

        testControllerMethod(path2, RestMethod.PUT, collectionDTO, getCorrectUser(), HttpStatus.NOT_FOUND);
    }

    private void testControllerMethod(String path, RestMethod method, CollectionDTO dto, UserPrincipal principal, HttpStatus expectedStatus) throws Exception {
        MockHttpServletResponse response = MockMvcHelper.perform(mvc, method, path, dto, principal);
        assertThat(response.getStatus()).isEqualTo(expectedStatus.value());
    }

    private void testControllerMethod(String path, RestMethod method, UserPrincipal principal, HttpStatus expectedStatus) throws Exception {
        testControllerMethod(path, method, null, principal, expectedStatus);
    }

    private UserPrincipal getCorrectUser() {
        return new UserPrincipal(1L, "Heniu");
    }

    private UserPrincipal getIncorrectUser() {
        return new UserPrincipal(2L, "Zbychu");
    }
}
