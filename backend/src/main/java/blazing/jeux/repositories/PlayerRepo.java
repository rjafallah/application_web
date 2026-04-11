package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import blazing.jeux.entity.Player;
import java.util.Optional;

@Repository
public interface PlayerRepo extends JpaRepository<Player,Long> {
    Optional<Player> findByUsername(String username);
}