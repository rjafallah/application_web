package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import blazing.jeux.entity.Game;
import blazing.jeux.entity.GamePlayer;
import blazing.jeux.entity.Player;
import java.util.List;
import java.util.Optional;

@Repository
public interface GamePlayerRepo extends JpaRepository<GamePlayer, Long> {
    List<GamePlayer> findByGame(Game game);
    Optional<GamePlayer> findByGameAndPlayer(Game game, Player player);
}