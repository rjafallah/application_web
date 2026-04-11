package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import blazing.jeux.entity.Deck;
import blazing.jeux.entity.Game;
import java.util.Optional;

@Repository
public interface DeckRepo extends JpaRepository<Deck, Long> {
    Optional<Deck> findByGame(Game game);
}