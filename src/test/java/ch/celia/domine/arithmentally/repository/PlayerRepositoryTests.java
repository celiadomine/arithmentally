package ch.celia.domine.arithmentally.repository;

import ch.celia.domine.arithmentally.player.Player;
import ch.celia.domine.arithmentally.player.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false) // so data stays in Postgres
public class PlayerRepositoryTests {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void insertPlayer() {
        Player player = new Player();
        player.setKeycloakId("1234");
        player.setName("TestUser");
        player.setEmail("test@user.com");
        player.setRole("read");

        Player saved = playerRepository.save(player);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("TestUser", saved.getName());
    }
}
