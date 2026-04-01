package blazing.jeux.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import blazing.jeux.entity.Card;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {

}