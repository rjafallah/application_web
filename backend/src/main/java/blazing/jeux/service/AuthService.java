package blazing.jeux.service;

import blazing.jeux.entity.Player;
import blazing.jeux.entity.Ranking;
import blazing.jeux.repositories.PlayerRepo;
import blazing.jeux.repositories.RankingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private RankingRepo rankingRepo;

    // inscription d'un nouveau joueur
    public Player register(String username, String password) {
        Optional<Player> existing = playerRepo.findByUsername(username);
        if (existing.isPresent()) {
            throw new RuntimeException("Nom d'utilisateur déjà pris");
        }
        Player player = new Player();
        player.setUsername(username);
        player.setPasswordHash(password);
        playerRepo.save(player);

        // créer le ranking associé
        Ranking ranking = new Ranking();
        ranking.setPlayer(player);
        ranking.setTotalGames(0);
        ranking.setTotalWins(0);
        ranking.setTotalLosses(0);
        rankingRepo.save(ranking);

        return player;
    }

    // connexion d'un joueur existant
    public Player login(String username, String password) {
        Optional<Player> player = playerRepo.findByUsername(username);
        if (player.isEmpty()) {
            throw new RuntimeException("Joueur introuvable");
        }
        if (!player.get().getPasswordHash().equals(password)) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return player.get();
    }
}