package blazing.jeux.controller;

import blazing.jeux.dto.CardDTO;
import blazing.jeux.dto.GameStateDTO;
import blazing.jeux.entity.*;
import blazing.jeux.repositories.*;
import blazing.jeux.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private GamePlayerRepo gamePlayerRepo;

    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private DeckRepo deckRepo;

    // démarrer une partie
    @PostMapping("/{gameId}/start")
    public String startGame(@PathVariable Long gameId) {
        gameService.startGame(gameId);
        return "Partie démarrée";
    }

    // état complet de la partie pour un joueur
    @GetMapping("/{gameId}")
    public GameStateDTO getGame(@PathVariable Long gameId, @RequestParam Long playerId) {
        Game game = gameService.getGame(gameId).orElseThrow(() -> new RuntimeException("Partie introuvable"));

        Player player = playerRepo.findById(playerId).orElseThrow(() -> new RuntimeException("Joueur introuvable"));

        GamePlayer currentGP = gamePlayerRepo.findByGameAndPlayer(game, player).orElseThrow(() -> new RuntimeException("Joueur introuvable dans la partie"));

        List<Card> hand = cardRepo.findByHolder(currentGP);

        Deck deck = deckRepo.findByGame(game).orElseThrow(() -> new RuntimeException("Deck introuvable"));

        Card topCard = cardRepo.findDefausse(deck).orElse(null);

        List<GamePlayer> allPlayers = gamePlayerRepo.findByGame(game);

        return new GameStateDTO(game, hand, topCard, allPlayers, currentGP);
    }

    // jouer une carte
    @PostMapping("/{gameId}/play")
    public String playCard(@PathVariable Long gameId, @RequestParam Long playerId, @RequestParam Long cardId, @RequestParam(required = false) String chosenColor) {
        Player player = playerRepo.findById(playerId).orElseThrow(() -> new RuntimeException("Joueur introuvable"));
        gameService.playCard(gameId, cardId, player, chosenColor);
        return "Carte jouée";
    }

    // piocher une carte
    @PostMapping("/{gameId}/draw")
    public CardDTO drawCard(@PathVariable Long gameId, @RequestParam Long playerId) {
        Player player = playerRepo.findById(playerId).orElseThrow(() -> new RuntimeException("Joueur introuvable"));
        Card card = gameService.drawCard(gameId, player);
        return new CardDTO(card);
    }
}