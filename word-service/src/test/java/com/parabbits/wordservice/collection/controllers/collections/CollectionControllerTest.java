package com.parabbits.wordservice.collection.controllers.collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parabbits.wordservice.collection.accessService.AccessResult;
import com.parabbits.wordservice.collection.accessService.CollectionAccessService;
import com.parabbits.wordservice.collection.accessService.UserAction;
import com.parabbits.wordservice.collection.controller.CollectionController;
import com.parabbits.wordservice.collection.controllers.advice.CommonAdvice;
import com.parabbits.wordservice.collection.data.CollectionFilter;
import com.parabbits.wordservice.collection.service.*;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    @Mock
    private CollectionAccessService accessService;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        CollectionController controller = new CollectionController(service, accessService);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new CommonAdvice())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldGetPrivateCollectionById() throws Exception {
        CollectionResponseDTO dto = getResponseDTO();
        when(service.getById(1L)).thenReturn(dto);
        MockHttpServletResponse response = testResponse(GET_BY_ID_PATH, null, UserAction.GET);
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(dto));
    }

    private MockHttpServletResponse testResponse(String path, CollectionDTO dto, UserAction action) throws Exception {
        RestMethod method = getRestMethod(action);
        when(accessService.checkAccess(1L, getCorrectUser(), action)).thenReturn(AccessResult.OK);
        MockHttpServletResponse okResponse = MockMvcHelper.perform(mvc, method, path, dto, getCorrectUser());
        assertThat(okResponse.getStatus()).isEqualTo(HttpStatus.OK.value());

        when(accessService.checkAccess(1L, getCorrectUser(), action)).thenReturn(AccessResult.NOT_FOUND);
        MockHttpServletResponse notFoundResponse = MockMvcHelper.perform(mvc, method, path, dto, getCorrectUser());
        assertThat(notFoundResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

        when(accessService.checkAccess(1L, getIncorrectUser(), action)).thenReturn(AccessResult.FORBIDDEN);
        MockHttpServletResponse forbiddenResponse = MockMvcHelper.perform(mvc, method, path, dto, getIncorrectUser());
        assertThat(forbiddenResponse.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());

        when(accessService.checkAccess(1L, null, action)).thenReturn(AccessResult.UNAUTHORIZED);
        MockHttpServletResponse unauthorizedResponse = MockMvcHelper.perform(mvc, method, path, dto, null);
        assertThat(unauthorizedResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        return okResponse;
    }

    private RestMethod getRestMethod(UserAction action) {
        Map<UserAction, RestMethod> map = new HashMap<>() {{
            put(UserAction.GET, RestMethod.GET);
            put(UserAction.CREATE, RestMethod.POST);
            put(UserAction.REMOVE, RestMethod.DELETE);
            put(UserAction.UPDATE, RestMethod.PUT);
        }};
        return map.get(action);
    }

    @Test
    public void shouldGetPublicCollection() throws Exception {
        CollectionResponseDTO publicDTO = getPublicCollectionDTO();
        when(service.getById(1L)).thenReturn(publicDTO);
        testPublicCollection(getCorrectUser());
        testPublicCollection(getIncorrectUser());
    }

    private void testPublicCollection(UserPrincipal principal) throws Exception {
        CollectionResponseDTO publicDTO = getPublicCollectionDTO();
        when(accessService.checkAccess(1L, principal, UserAction.GET)).thenReturn(AccessResult.OK);
        MockHttpServletResponse response = MockMvcHelper.perform(mvc, RestMethod.GET, GET_BY_ID_PATH, principal);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(publicDTO));
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
        UserPrincipal principal = getCorrectUser();
        when(accessService.checkAccess(CollectionAccess.getAccess(null, null), principal, UserAction.GET)).thenReturn(AccessResult.OK);
        CollectionFilter nameFilter = CollectionFilter.builder().name("kaszanka").build();
        testGetByFilter(namePath, nameFilter, principal);

        String language1Path = COLLECTION_PATH + "?language1=1";
        CollectionFilter language1Filter = CollectionFilter.builder().language1(1L).build();
        testGetByFilter(language1Path, language1Filter, principal);

        String userPath = COLLECTION_PATH + "?userId=1";
        when(accessService.checkAccess(CollectionAccess.getAccess(null, 1L), principal, UserAction.GET)).thenReturn(AccessResult.OK);
        CollectionFilter userFilter = CollectionFilter.builder().userId(1L).build();
        testGetByFilter(userPath, userFilter, principal);

        String manyPath = COLLECTION_PATH + "?name=kaszanka&userId=1";
        when(accessService.checkAccess(CollectionAccess.getAccess(null, 1L), principal, UserAction.GET)).thenReturn(AccessResult.OK);
        CollectionFilter manyFilter = CollectionFilter.builder().name("kaszanka").userId(1L).build();
        testGetByFilter(manyPath, manyFilter, principal);

    }

    private void testGetByFilter(String path, CollectionFilter filter, UserPrincipal user) throws Exception {
        when(service.getByFilter(filter)).thenReturn(Collections.singletonList(getResponseDTO()));
        MockHttpServletResponse response = MockMvcHelper.perform(mvc, RestMethod.GET, path, user);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(Arrays.asList(getResponseDTO())));
    }

    @Test
    public void shouldBlockGetCollectionWhenIncorrectUser() throws Exception {
        String path = COLLECTION_PATH + "?userId=1&isPublic=false";
        UserPrincipal principal = getIncorrectUser();
        when(accessService.checkAccess(CollectionAccess.getAccess(false, 1L), principal, UserAction.GET)).thenReturn(AccessResult.FORBIDDEN);
        MockHttpServletResponse response = MockMvcHelper.perform(mvc, RestMethod.GET, path, principal);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void shouldAddNewCollection() throws Exception {
        long userId = 1;
        CollectionDTO dto = new CollectionDTO(null, "One", null, 1, 3, userId, false);
        when(accessService.checkAccess(CollectionAccess.ownerAccess(1L), getCorrectUser(), UserAction.CREATE)).thenReturn(AccessResult.OK);
        MockHttpServletResponse response = MockMvcHelper.perform(mvc, RestMethod.POST, COLLECTION_PATH, dto, getCorrectUser());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void shouldThrowErrorWhenAddNewCollectionWithIncorrectData() throws Exception {
        long userId = 1;
        CollectionDTO dto = new CollectionDTO(null, "One", null, 1, 200, userId, false);
        when(service.addCollection(dto)).thenThrow(new IllegalArgumentException());
        when(accessService.checkAccess(CollectionAccess.ownerAccess(1L), getCorrectUser(), UserAction.CREATE)).thenReturn(AccessResult.OK);
        MockHttpServletResponse response = MockMvcHelper.perform(mvc, RestMethod.POST, COLLECTION_PATH, dto, getCorrectUser());
        // TODO: przemysleć, co tutaj powinno być
        assertThat(response.getStatus()).isEqualTo(HttpStatus.PRECONDITION_FAILED.value());
    }

    @Test
    public void shouldBlockAddNewCollectionWithIncorrectUser() throws Exception {
        long userId = 1;
        CollectionDTO dto = new CollectionDTO(null, "One", null, 1, 200, userId, false);
        when(accessService.checkAccess(CollectionAccess.ownerAccess(1L), getIncorrectUser(), UserAction.CREATE)).thenReturn(AccessResult.FORBIDDEN);
        MockHttpServletResponse response = MockMvcHelper.perform(mvc, RestMethod.POST, COLLECTION_PATH, dto, getIncorrectUser());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void shouldRemoveCollection() throws Exception {
        final String path = COLLECTION_PATH + "/1";
        testResponse(path, null, UserAction.REMOVE);
    }

    @Test
    public void shouldUpdateCollection() throws Exception {
        final String path = COLLECTION_PATH + "/1";
        CollectionDTO collectionDTO = new CollectionDTO(1L, "One", null, 1L, 2L, 1L, true);
        testResponse(path, collectionDTO, UserAction.UPDATE);
    }

    private UserPrincipal getCorrectUser() {
        return new UserPrincipal(1L, "Heniu");
    }

    private UserPrincipal getIncorrectUser() {
        return new UserPrincipal(2L, "Zbychu");
    }
}