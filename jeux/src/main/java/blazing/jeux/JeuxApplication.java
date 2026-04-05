package blazing.jeux;

import blazing.jeux.entity.Card;
import blazing.jeux.entity.Game;
import blazing.jeux.entity.GamePlayer;
import blazing.jeux.entity.Player;
import blazing.jeux.entity.Deck;
import blazing.jeux.repositories.CardRepo;
import blazing.jeux.repositories.GamePlayerRepo;
import blazing.jeux.repositories.GameRepo;
import blazing.jeux.repositories.DeckRepo;
import blazing.jeux.service.AuthService;
import blazing.jeux.service.GameService;
import blazing.jeux.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import java.util.List;



@SpringBootApplication
public class JeuxApplication {

	public static void main(String[] args) {
		SpringApplication.run(JeuxApplication.class, args);
	}

	@Autowired private AuthService authService;
    @Autowired private GameService gameService;
    @Autowired private RankingService rankingService;
    @Autowired private CardRepo cardRepo;
    @Autowired private GamePlayerRepo gamePlayerRepo;
    @Autowired private GameRepo gameRepo;
    @Autowired private DeckRepo deckRepo;

	@PostConstruct
    public void test() {
        try {
            // ============================================
            // TEST 1 : inscription et connexion
            // ============================================
            System.out.println("\n=== TEST 1 : AUTH ===");
            Player p1 = authService.register("houssam", "1234");
            Player p2 = authService.register("amine", "1234");
            Player p3 = authService.register("abdelhamid", "1234");
            Player p4 = authService.register("khalil", "1234");
            System.out.println("Register OK : " + p1.getUsername() + ", "
                    + p2.getUsername() + ", " + p3.getUsername() + ", " + p4.getUsername());

            Player login = authService.login("houssam", "1234");
            System.out.println("Login OK : " + login.getUsername());

            // mauvais mot de passe
            try {
                authService.login("houssam", "mauvais");
            } catch (RuntimeException e) {
                System.out.println("Login refusé OK : " + e.getMessage());
            }

            // username déjà pris
            try {
                authService.register("houssam", "5678");
            } catch (RuntimeException e) {
                System.out.println("Register refusé OK : " + e.getMessage());
            }

            // ============================================
            // TEST 2 : création et rejoindre une partie
            // ============================================
            System.out.println("\n=== TEST 2 : CREATION PARTIE ===");
            Game game = gameService.createGame(p1);
            System.out.println("Partie créée OK : id=" + game.getId() + " status=" + game.getStatus());

            gameService.joinGame(game.getId(), p2);
            gameService.joinGame(game.getId(), p3);
            gameService.joinGame(game.getId(), p4);
            System.out.println("3 joueurs rejoints OK");

            // test max joueurs
            try {
                Player p5 = authService.register("extra", "1234");
                gameService.joinGame(game.getId(), p5);
            } catch (RuntimeException e) {
                System.out.println("Max joueurs OK : " + e.getMessage());
            }

            // ============================================
            // TEST 3 : démarrage et vérification des cartes
            // ============================================
            System.out.println("\n=== TEST 3 : DEMARRAGE ===");
            game = gameService.startGame(game.getId());
            System.out.println("Partie démarrée OK : status=" + game.getStatus());
            System.out.println("Premier joueur : " + game.getCurrentPlayer().getUsername());
            System.out.println("Couleur active : " + game.getCurrentColor());

            // vérifier les mains (5 cartes par joueur)
            List<GamePlayer> gamePlayers = gamePlayerRepo.findByGame(game);
            for (GamePlayer gp : gamePlayers) {
                List<Card> hand = cardRepo.findByHolder(gp);
                System.out.println("Main de " + gp.getPlayer().getUsername()
                        + " : " + hand.size() + " cartes");
            }

            // récupérer le deck depuis la BDD
            Deck deck = deckRepo.findByGame(game)
                    .orElseThrow(() -> new RuntimeException("Deck introuvable"));

            // vérifier la défausse
            Card defausse = cardRepo.findDefausse(deck)
                    .orElseThrow(() -> new RuntimeException("Pas de défausse"));
            System.out.println("Carte défausse : "
                    + defausse.getValue() + " " + defausse.getColor());

            // vérifier la pioche
            List<Card> pioche = cardRepo.findPioche(deck);
            System.out.println("Cartes restantes en pioche : " + pioche.size());

            // ============================================
            // TEST 4 : jouer un tour
            // ============================================
            System.out.println("\n=== TEST 4 : JOUER UN TOUR ===");
            Player currentPlayer = game.getCurrentPlayer();
            GamePlayer currentGP = gamePlayerRepo.findByGameAndPlayer(game, currentPlayer)
                    .orElseThrow(() -> new RuntimeException("Joueur introuvable"));
            List<Card> hand = cardRepo.findByHolder(currentGP);

            System.out.println("Tour de : " + currentPlayer.getUsername());
            System.out.println("Sa main :");
            for (Card c : hand) {
                System.out.println("  - " + c.getValue() + " " + c.getColor());
            }

            // chercher une carte jouable
            Card cardToPlay = null;
            for (Card c : hand) {
                if (gameService.isValidPlay(c, defausse, game.getCurrentColor())) {
                    cardToPlay = c;
                    break;
                }
            }

            if (cardToPlay != null) {
                System.out.println("Carte jouée : "
                        + cardToPlay.getValue() + " " + cardToPlay.getColor());
                String chosenColor = cardToPlay.getValue().equals("8") ? "ROUGE" : null;
                game = gameService.playCard(game.getId(), cardToPlay.getId(),
                        currentPlayer, chosenColor);
                System.out.println("Coup joué OK");
                System.out.println("Joueur suivant : " + game.getCurrentPlayer().getUsername());
                System.out.println("Nouvelle couleur : " + game.getCurrentColor());
            } else {
                // piocher si aucune carte jouable
                Card drawn = gameService.drawCard(game.getId(), currentPlayer);
                System.out.println("Aucune carte jouable → pioche : "
                        + drawn.getValue() + " " + drawn.getColor());
            }

            // ============================================
            // TEST 5 : ranking
            // ============================================
            System.out.println("\n=== TEST 5 : RANKING ===");
            rankingService.recordWin(p1);
            rankingService.recordLoss(p2);
            rankingService.recordLoss(p3);
            rankingService.recordLoss(p4);
            rankingService.getFullRanking().forEach(r ->
                System.out.println(r.getPlayer().getUsername()
                    + " → wins=" + r.getTotalWins()
                    + " losses=" + r.getTotalLosses()
                    + " games=" + r.getTotalGames())
            );

            System.out.println("\n=== TOUS LES TESTS PASSES ===");

        } catch (Exception e) {
            System.out.println("\n=== ERREUR : " + e.getMessage() + " ===");
            e.printStackTrace();
        }
    }
}
