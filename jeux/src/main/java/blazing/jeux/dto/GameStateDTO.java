// package blazing.jeux.dto;

// import blazing.jeux.entity.*;
// import blazing.jeux.repositories.CardRepo;

// import java.util.List;
// import java.util.stream.Collectors;

// public class GameStateDTO {
//     private Long gameId;
//     private String status;
//     private String direction;
//     private String currentColor;
//     private PlayerDTO currentPlayer;
//     private List<CardDTO> hand;
//     private CardDTO topCard;
//     private List<OpponentDTO> opponents;

//     public GameStateDTO() {}

//     public Long getGameId() { return gameId; }
//     public void setGameId(Long gameId) { this.gameId = gameId; }

//     public String getStatus() { return status; }
//     public void setStatus(String status) { this.status = status; }

//     public String getDirection() { return direction; }
//     public void setDirection(String direction) { this.direction = direction; }

//     public String getCurrentColor() { return currentColor; }
//     public void setCurrentColor(String currentColor) { this.currentColor = currentColor; }

//     public PlayerDTO getCurrentPlayer() { return currentPlayer; }
//     public void setCurrentPlayer(PlayerDTO currentPlayer) { this.currentPlayer = currentPlayer; }

//     public List<CardDTO> getHand() { return hand; }
//     public void setHand(List<CardDTO> hand) { this.hand = hand; }

//     public CardDTO getTopCard() { return topCard; }
//     public void setTopCard(CardDTO topCard) { this.topCard = topCard; }

//     public List<OpponentDTO> getOpponents() { return opponents; }
//     public void setOpponents(List<OpponentDTO> opponents) { this.opponents = opponents; }

//     // classe interne pour représenter un adversaire
//     public static class OpponentDTO {
//         private String username;
//         private int handSize;
//         private boolean unoCall;

//         public OpponentDTO() {}
//         public OpponentDTO(String username, int handSize, boolean unoCall) {
//             this.username = username;
//             this.handSize = handSize;
//             this.unoCall = unoCall;
//         }

//         public String getUsername() { return username; }
//         public void setUsername(String username) { this.username = username; }

//         public int getHandSize() { return handSize; }
//         public void setHandSize(int handSize) { this.handSize = handSize; }

//         public boolean isUnoCall() { return unoCall; }
//         public void setUnoCall(boolean unoCall) { this.unoCall = unoCall; }
//     }
// }
