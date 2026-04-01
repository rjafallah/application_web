package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import blazing.jeux.entity.Game;

@Repository
public interface GameRepo extends JpaRepository<Game, Long> {

}