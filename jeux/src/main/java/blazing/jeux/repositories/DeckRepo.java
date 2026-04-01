package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import blazing.jeux.entity.Deck;

@Repository
public interface DeckRepo extends JpaRepository<Deck, Long> {

}