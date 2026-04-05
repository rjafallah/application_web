package blazing.jeux.service;

import blazing.jeux.entity.*;
import blazing.jeux.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepo gameRepo;

    @Autowired
    private GamePlayerRepo gamePlayerRepo;

    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private GameTurnRepo gameTurnRepo;

    @Autowired
    private DeckService deckService;

    // créer une nouvelle partie
    public Game createGame(Player player) {
        Game game = new Game();
        game.setStatus("WAITING");
        game.setDirection("CLOCKWISE");
        game.setCurrentColor(null);
        game.setCurrentPlayer(null);
        gameRepo.save(game);

        GamePlayer gp = new GamePlayer();
        gp.setGame(game);
        gp.setPlayer(player);
        gamePlayerRepo.save(gp);

        return game;
    }

    // rejoindre une partie en attente
    public Game joinGame(Long gameId, Player player) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new RuntimeException("Partie introuvable"));

        if (!game.getStatus().equals("WAITING")) {
            throw new RuntimeException("La partie a déjà commencé");
        }

        // max 4 joueurs
        List<GamePlayer> players = gamePlayerRepo.findByGame(game);
        if (players.size() >= 4) {
            throw new RuntimeException("La partie est complète (max 4 joueurs)");
        }

        GamePlayer gp = new GamePlayer();
        gp.setGame(game);
        gp.setPlayer(player);
        gamePlayerRepo.save(gp);
        return game;
    }

    // démarrer la partie
    public Game startGame(Long gameId) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new RuntimeException("Partie introuvable"));

        List<GamePlayer> players = gamePlayerRepo.findByGame(game);
        if (players.size() < 2) {
            throw new RuntimeException("Il faut au moins 2 joueurs");
        }

        Deck deck = deckService.createDeck(game);
        deckService.distributeCards(deck, players);

        game.setStatus("IN_PROGRESS");
        game.setCurrentPlayer(players.get(0).getPlayer());

        // couleur initiale = couleur de la première carte de la défausse
        Card topCard = cardRepo.findDefausse(deck).orElseThrow(() -> new RuntimeException("Aucune carte sur la défausse"));
        game.setCurrentColor(topCard.getColor());

        gameRepo.save(game);
        return game;
    }

    // vérifier si une carte peut être jouée
    public boolean isValidPlay(Card played, Card topCard, String currentColor) {
        // 8 et OSCARS_SWAP sont jouables à tout moment
        if (played.getValue().equals("8")) return true;
        if (played.getValue().equals("OSCARS_SWAP")) return true;

        // GIDDY_UP et WRONG_WAY : même couleur que la couleur active
        if (played.getValue().equals("GIDDY_UP") || played.getValue().equals("WRONG_WAY")) {
            return played.getColor().equals(currentColor);
        }

        // carte normale : même couleur ou même valeur
        if (played.getColor().equals(currentColor)) return true;
        if (played.getValue().equals(topCard.getValue())) return true;

        return false;
    }

    // jouer une carte
    public Game playCard(Long gameId, Long cardId, Player player, String chosenColor) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new RuntimeException("Partie introuvable"));

        // vérifier que c'est le tour de ce joueur
        if (!game.getCurrentPlayer().getId().equals(player.getId())) {
            throw new RuntimeException("Ce n'est pas votre tour");
        }

        Card card = cardRepo.findById(cardId).orElseThrow(() -> new RuntimeException("Carte introuvable"));

        Card topCard = cardRepo.findDefausse(game.getDeck()).orElseThrow(() -> new RuntimeException("Aucune carte sur la défausse"));

        if (!isValidPlay(card, topCard, game.getCurrentColor())) {
            throw new RuntimeException("Coup invalide");
        }

        // retirer l'ancienne carte de la défausse
        topCard.setDiscard(false);
        cardRepo.save(topCard);

        // poser la nouvelle carte sur la défausse
        card.setHolder(null);
        card.setDiscard(true);
        cardRepo.save(card);

        List<GamePlayer> players = gamePlayerRepo.findByGame(game);
        int currentIndex = getCurrentPlayerIndex(players, player);
        int nextIndex = getNextIndex(game, players, currentIndex);

        // appliquer les effets de la carte
        switch (card.getValue()) {

            case "8":
                // Wild 8 : le joueur choisit la couleur suivante
                if (chosenColor == null || chosenColor.isEmpty()) {
                    throw new RuntimeException("Vous devez choisir une couleur pour le 8");
                }
                game.setCurrentColor(chosenColor);
                break;

            case "OSCARS_SWAP":
                // échanger sa main avec le joueur suivant
                GamePlayer nextGP = players.get(nextIndex);
                GamePlayer currentGP = gamePlayerRepo.findByGameAndPlayer(game, player).orElseThrow(() -> new RuntimeException("Joueur introuvable"));
                swapHands(currentGP, nextGP);
                // la couleur reste celle de la carte du dessus
                game.setCurrentColor(topCard.getColor());
                break;

            case "GIDDY_UP":
                // bloquer le joueur suivant : on saute un tour de plus
                nextIndex = getNextIndex(game, players, nextIndex);
                game.setCurrentColor(card.getColor());
                break;

            case "WRONG_WAY":
                // inverser le sens du jeu
                game.setDirection(game.getDirection().equals("CLOCKWISE") ? "COUNTERCLOCKWISE" : "CLOCKWISE");
                // recalculer le suivant avec le nouveau sens
                nextIndex = getNextIndex(game, players, currentIndex);
                game.setCurrentColor(card.getColor());
                break;

            default:
                // carte normale : la couleur devient celle de la carte
                game.setCurrentColor(card.getColor());
                break;
        }

        // passer au joueur suivant
        game.setCurrentPlayer(players.get(nextIndex).getPlayer());

        // enregistrer le tour dans l'historique
        GameTurn turn = new GameTurn();
        turn.setGame(game);
        turn.setPlayer(player);
        turn.setCard(card);
        gameTurnRepo.save(turn);

        // vérifier si le joueur a gagné (main vide)
        GamePlayer winnerCheck = gamePlayerRepo.findByGameAndPlayer(game, player).orElseThrow(() -> new RuntimeException("Joueur introuvable"));
        if (cardRepo.findByHolder(winnerCheck).isEmpty()) {
            game.setStatus("FINISHED");
        }

        gameRepo.save(game);
        return game;
    }

    // échanger les mains de deux joueurs
    private void swapHands(GamePlayer gp1, GamePlayer gp2) {
        List<Card> hand1 = cardRepo.findByHolder(gp1);
        List<Card> hand2 = cardRepo.findByHolder(gp2);

        // donner les cartes de gp1 à gp2
        for (Card c : hand1) {
            c.setHolder(gp2);
            cardRepo.save(c);
        }

        // donner les cartes de gp2 à gp1
        for (Card c : hand2) {
            c.setHolder(gp1);
            cardRepo.save(c);
        }
    }

    // piocher une carte
    public Card drawCard(Long gameId, Player player) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new RuntimeException("Partie introuvable"));
        GamePlayer gp = gamePlayerRepo.findByGameAndPlayer(game, player).orElseThrow(() -> new RuntimeException("Joueur introuvable dans la partie"));
        return deckService.drawCard(game.getDeck(), gp);
    }

    // récupérer les cartes en main d'un joueur
    public List<Card> getPlayerHand(Long gameId, Player player) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new RuntimeException("Partie introuvable"));
        GamePlayer gp = gamePlayerRepo.findByGameAndPlayer(game, player).orElseThrow(() -> new RuntimeException("Joueur introuvable dans la partie"));
        return cardRepo.findByHolder(gp);
    }

    // trouver l'index du joueur actuel dans la liste
    private int getCurrentPlayerIndex(List<GamePlayer> players, Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayer().getId().equals(player.getId())) return i;
        }
        return 0;
    }

    // calculer l'index du joueur suivant selon la direction
    private int getNextIndex(Game game, List<GamePlayer> players, int current) {
        if (game.getDirection().equals("CLOCKWISE")) {
            return (current + 1) % players.size();
        } else {
            return (current - 1 + players.size()) % players.size();
        }
    }

    // récupérer les parties en attente pour le lobby
    public List<Game> getWaitingGames() {
        return gameRepo.findByStatus("WAITING");
    }

    // récupérer une partie par son id
    public Optional<Game> getGame(Long gameId) {
        return gameRepo.findById(gameId);
    }
}