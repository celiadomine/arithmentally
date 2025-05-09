package ch.celia.domine.arithmentally;

import ch.celia.domine.arithmentally.player.Player;
import ch.celia.domine.arithmentally.player.PlayerRepository;
import ch.celia.domine.arithmentally.player.PlayerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTests {

    private PlayerService playerService;
    private PlayerRepository playerRepositoryMock;

    @BeforeEach
    void setUp() {
        playerRepositoryMock = mock(PlayerRepository.class);
        playerService = new PlayerService(playerRepositoryMock);
    }

    @Test
    void getOrCreatePlayer_returnsExistingPlayerIfFound() {
        Player existingPlayer = new Player();
        existingPlayer.setKeycloakId("abc123");
        when(playerRepositoryMock.findByKeycloakId("abc123")).thenReturn(Optional.of(existingPlayer));

        Player result = playerService.getOrCreatePlayer("abc123", "admin", "admin@test.com");

        assertEquals(existingPlayer, result);
        verify(playerRepositoryMock, never()).save(any());
    }

    @Test
    void getOrCreatePlayer_createsNewPlayerIfNotFound() {
        when(playerRepositoryMock.findByKeycloakId("abc123")).thenReturn(Optional.empty());

        when(playerRepositoryMock.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Player result = playerService.getOrCreatePlayer("abc123", "newuser", "newuser@test.com");

        assertEquals("abc123", result.getKeycloakId());
        assertEquals("newuser", result.getName());
        assertEquals("newuser@test.com", result.getEmail());
        assertEquals("USER", result.getRole());

        verify(playerRepositoryMock).save(any());
    }
}
