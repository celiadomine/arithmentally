package ch.celia.domine.arithmentally.player;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Find player by their Keycloak UUID
    Optional<Player> findByKeycloakId(String keycloakId);
}