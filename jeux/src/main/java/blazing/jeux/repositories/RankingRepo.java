package blazing.jeux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blazing.jeux.entity.Ranking;

@Repository
public interface RankingRepo extends JpaRepository<Ranking,Long> {

}