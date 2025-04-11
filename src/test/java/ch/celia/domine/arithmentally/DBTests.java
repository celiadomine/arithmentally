package ch.celia.domine.arithmentally;

import ch.celia.domine.arithmentally.player.Player;
import ch.celia.domine.arithmentally.player.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void insertPlayer() {
        Player player = new Player();
        player.setKeycloakId("test-keycloak-id");
        player.setName("Test Player");
        player.setEmail("test@example.com");
        player.setRole("USER");

        Player saved = playerRepository.save(player);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Test Player", saved.getName());
    }
}
