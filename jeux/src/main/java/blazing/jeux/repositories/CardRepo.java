package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import blazing.jeux.entity.Card;
import blazing.jeux.entity.Deck;
import blazing.jeux.entity.GamePlayer;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {

    // cartes dans la pioche
    @Query("SELECT c FROM Card c WHERE c.deck = :deck AND c.holder IS NULL AND c.discard = false")
    List<Card> findPioche(@Param("deck") Deck deck);

    // carte sur la défausse
    @Query("SELECT c FROM Card c WHERE c.deck = :deck AND c.discard = true")
    Optional<Card> findDefausse(@Param("deck") Deck deck);

    // cartes en main d'un joueur
    List<Card> findByHolder(GamePlayer holder);
}