package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import blazing.jeux.entity.Game;
import blazing.jeux.entity.Message;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findByGameOrderBySentAtAsc(Game game);
}