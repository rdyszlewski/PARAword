package com.parabbits.wordservice.collection.data.collection;

import com.parabbits.wordservice.collection.accessService.AccessResult;
import com.parabbits.wordservice.collection.accessService.CollectionAccessService;
import com.parabbits.wordservice.collection.accessService.UserAction;
import com.parabbits.wordservice.collection.service.CollectionAccess;
import com.parabbits.wordservice.collection.service.CollectionService;
import com.parabbits.wordservice.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CollectionAccessServiceTest {

    @Mock
    private CollectionService service;

    private CollectionAccessService accessService;

    @BeforeEach
    public void initTest() {
        accessService = new CollectionAccessService(service);
    }

    @Test
    public void shouldAccessGetAction() {
        when(service.getCollectionAccess(1L)).thenReturn(getPrivateAccess());
        assertThat(accessService.checkAccess(1L, getCorrectUser(), UserAction.GET)).isEqualTo(AccessResult.OK);
        assertThat(accessService.checkAccess(1L, getIncorrectUser(), UserAction.GET)).isEqualTo(AccessResult.FORBIDDEN);
        assertThat(accessService.checkAccess(1L, null, UserAction.GET)).isEqualTo(AccessResult.UNAUTHORIZED);

        when(service.getCollectionAccess(1L)).thenReturn(getPublicAccess());
        assertThat(accessService.checkAccess(1L, getCorrectUser(), UserAction.GET)).isEqualTo(AccessResult.OK);
        assertThat(accessService.checkAccess(1L, getIncorrectUser(), UserAction.GET)).isEqualTo(AccessResult.OK);

        when(service.getCollectionAccess(1L)).thenReturn(getNotFoundAccess());
        assertThat(accessService.checkAccess(1L, getCorrectUser(), UserAction.GET)).isEqualTo(AccessResult.NOT_FOUND);
    }

    private UserPrincipal getCorrectUser() {
        return new UserPrincipal(1L, "Heniu");
    }

    private UserPrincipal getIncorrectUser() {
        return new UserPrincipal(2L, "Zbychu");
    }

    private CollectionAccess getPrivateAccess() {
        return CollectionAccess.getAccess(false, 1L);
    }

    private CollectionAccess getPublicAccess() {
        return CollectionAccess.getAccess(true, 1L);
    }

    private CollectionAccess getNotFoundAccess() {
        return CollectionAccess.notFound();
    }
}
