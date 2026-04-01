package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import blazing.jeux.entity.Message;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

}