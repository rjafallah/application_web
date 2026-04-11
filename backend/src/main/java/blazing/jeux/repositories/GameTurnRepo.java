package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import blazing.jeux.entity.Game;
import blazing.jeux.entity.GameTurn;
import java.util.List;

@Repository
public interface GameTurnRepo extends JpaRepository<GameTurn, Long>  {
    List<GameTurn> findByGame(Game game);
}