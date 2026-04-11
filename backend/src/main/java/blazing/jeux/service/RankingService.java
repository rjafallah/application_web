package blazing.jeux.service;

import blazing.jeux.entity.Player;
import blazing.jeux.entity.Ranking;
import blazing.jeux.repositories.RankingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    @Autowired
    private RankingRepo rankingRepo;

    // enregistrer une victoire
    public void recordWin(Player player) {
        Ranking ranking = rankingRepo.findByPlayer(player).orElseThrow(() -> new RuntimeException("Ranking introuvable"));
        ranking.setTotalWins(ranking.getTotalWins() + 1);
        ranking.setTotalGames(ranking.getTotalGames() + 1);
        rankingRepo.save(ranking);
    }

    // enregistrer une défaite
    public void recordLoss(Player player) {
        Ranking ranking = rankingRepo.findByPlayer(player).orElseThrow(() -> new RuntimeException("Ranking introuvable"));
        ranking.setTotalLosses(ranking.getTotalLosses() + 1);
        ranking.setTotalGames(ranking.getTotalGames() + 1);
        rankingRepo.save(ranking);
    }

    // récupérer le classement trié par victoires
    public List<Ranking> getFullRanking() {
        return rankingRepo.findClassement();
    }
}