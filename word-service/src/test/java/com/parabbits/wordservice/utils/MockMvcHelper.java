package com.parabbits.wordservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parabbits.wordservice.security.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockMvcHelper {

    public static MockHttpServletResponse perform(MockMvc mvc, RestMethod method, String path, Object dto, UserPrincipal principal) throws Exception {
        MockHttpServletRequestBuilder builder = getBuilder(method, path);
        builder.contentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        if (dto != null) {
            builder.content(objectMapper.writeValueAsString(dto));
        }
        if (principal != null) {
            builder.principal(principal);
        }
        return mvc.perform(builder).andReturn().getResponse();
    }

    public static MockHttpServletResponse perform(MockMvc mvc, RestMethod method, String path, UserPrincipal principal) throws Exception {
        return perform(mvc, method, path, null, principal);
    }

    public static MockHttpServletResponse perform(MockMvc mvc, RestMethod method, String path) throws Exception {
        return perform(mvc, method, path, null, null);
    }

    private static MockHttpServletRequestBuilder getBuilder(RestMethod method, String path) {
        switch (method) {
            case GET:
                return MockMvcRequestBuilders.get(path);
            case POST:
                return MockMvcRequestBuilders.post(path);
            case DELETE:
                return MockMvcRequestBuilders.delete(path);
            case PUT:
                return MockMvcRequestBuilders.put(path);
            default:
                throw new IllegalArgumentException();
        }
    }
}
