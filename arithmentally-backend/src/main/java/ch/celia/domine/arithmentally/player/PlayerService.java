package ch.celia.domine.arithmentally.player;

import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public Player getOrCreatePlayer(String keycloakId, String name, String email) {
        return repository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    Player player = new Player();
                    player.setKeycloakId(keycloakId);
                    player.setName(name);
                    player.setEmail(email);
                    player.setRole("USER");
                    return repository.save(player);
                });
    }
    
    public void deletePlayerById(Long id) {
        repository.deleteById(id);
    }

    public Player updatePlayerById(Long id, Player updatedPlayer) {
        return repository.findById(id)
            .map(player -> {
                player.setName(updatedPlayer.getName());
                player.setEmail(updatedPlayer.getEmail());
                player.setRole(updatedPlayer.getRole());
                return repository.save(player);
            })
            .orElseThrow(() -> new RuntimeException("Player not found with ID: " + id));
    }
    
}
