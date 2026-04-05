package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import blazing.jeux.entity.Player;
import blazing.jeux.entity.Ranking;
import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepo extends JpaRepository<Ranking, Long> {
    Optional<Ranking> findByPlayer(Player player);

    @Query("SELECT r FROM Ranking r ORDER BY r.totalWins DESC")
    List<Ranking> findClassement();
}