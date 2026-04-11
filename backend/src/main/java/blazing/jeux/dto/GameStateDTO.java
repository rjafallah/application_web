package blazing.jeux.dto;

import blazing.jeux.entity.Card;
import blazing.jeux.entity.Game;
import blazing.jeux.entity.GamePlayer;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameStateDTO {
    private Long gameId;
    private String status;
    private String direction;
    private String currentColor;
    private PlayerDTO currentPlayer;
    private List<CardDTO> hand;
    private CardDTO topCard;
    private List<OpponentDTO> opponents;

    public GameStateDTO() {}

    // constructeur principal utilisé dans le controller
    public GameStateDTO(Game game, List<Card> playerHand, Card topCard, List<GamePlayer> allPlayers, GamePlayer currentGP) {
        this.gameId = game.getId();
        this.status = game.getStatus();
        this.direction = game.getDirection();
        this.currentColor = game.getCurrentColor();
        this.currentPlayer = new PlayerDTO(game.getCurrentPlayer());

        // main du joueur connecté
        this.hand = playerHand.stream().map(card -> new CardDTO(card)).collect(Collectors.toList());

        // carte du dessus de la défausse
        this.topCard = topCard != null ? new CardDTO(topCard) : null;

        // adversaires avec leur nombre de cartes
       List<OpponentDTO> opponents = new ArrayList<>();
        for (GamePlayer gp : allPlayers) {
            if (!gp.getId().equals(currentGP.getId())) {
                opponents.add(new OpponentDTO(
                    gp.getPlayer().getUsername(),
                    gp.getHandSize()
                ));
            }
        }
        this.opponents = opponents;
    }

    public Long getGameId() { 
        return gameId; 
    }

    public void setGameId(Long gameId) { 
        this.gameId = gameId; 
    }

    public String getStatus() { 
        return status; 
    }

    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getDirection() { 
        return direction; 
    }

    public void setDirection(String direction) { 
        this.direction = direction; 
    }

    public String getCurrentColor() { 
        return currentColor; 
    }

    public void setCurrentColor(String currentColor) { 
        this.currentColor = currentColor; 
    }

    public PlayerDTO getCurrentPlayer() { 
        return currentPlayer; 
    }

    public void setCurrentPlayer(PlayerDTO currentPlayer) { 
        this.currentPlayer = currentPlayer; 
    }

    public List<CardDTO> getHand() { 
        return hand; 
    }

    public void setHand(List<CardDTO> hand) { 
        this.hand = hand; 
    }

    public CardDTO getTopCard() { 
        return topCard; 
    }

    public void setTopCard(CardDTO topCard) { 
        this.topCard = topCard; 
    }

    public List<OpponentDTO> getOpponents() { 
        return opponents; 
    }

    public void setOpponents(List<OpponentDTO> opponents) { 
        this.opponents = opponents; 
    }

    public static class OpponentDTO {
        private String username;
        private int handSize;

        public OpponentDTO() {}

        public OpponentDTO(String username, int handSize) {
            this.username = username;
            this.handSize = handSize;
        }

        public String getUsername() { 
            return username; 
        }

        public void setUsername(String username) { 
            this.username = username; 
        }

        public int getHandSize() { 
            return handSize; 
        }

        public void setHandSize(int handSize) { 
            this.handSize = handSize; 
        }
    }
}