package blazing.jeux.websocket;

import blazing.jeux.dto.GameStateDTO;
import blazing.jeux.entity.*;
import blazing.jeux.repositories.*;
import blazing.jeux.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/game/{gameId}/{playerId}")
public class GameEndpoint {

    // stocke les sessions par gameId
    // ex: gameId=1 → [session joueur1, session joueur2, ...]
    private static Map<Long, Set<Session>> gameSessions = new ConcurrentHashMap<>();

    private static GameService gameService;
    private static PlayerRepo playerRepo;
    private static GamePlayerRepo gamePlayerRepo;
    private static CardRepo cardRepo;
    private static DeckRepo deckRepo;
    private static ObjectMapper objectMapper = new ObjectMapper();

    // injection via setter car @ServerEndpoint n'est pas géré par Spring directement
    @Autowired
    public void setGameService(GameService gameService) {
        GameEndpoint.gameService = gameService;
    }

    @Autowired
    public void setPlayerRepo(PlayerRepo playerRepo) {
        GameEndpoint.playerRepo = playerRepo;
    }

    @Autowired
    public void setGamePlayerRepo(GamePlayerRepo gamePlayerRepo) {
        GameEndpoint.gamePlayerRepo = gamePlayerRepo;
    }

    @Autowired
    public void setCardRepo(CardRepo cardRepo) {
        GameEndpoint.cardRepo = cardRepo;
    }

    @Autowired
    public void setDeckRepo(DeckRepo deckRepo) {
        GameEndpoint.deckRepo = deckRepo;
    }

    // un joueur se connecte au WebSocket
    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") Long gameId, @PathParam("playerId") Long playerId) throws IOException {
        // ajouter la session dans la map
        gameSessions.computeIfAbsent(gameId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        // stocker gameId et playerId dans la session pour les retrouver plus tard
        session.getUserProperties().put("gameId", gameId);
        session.getUserProperties().put("playerId", playerId);

        System.out.println("Joueur " + playerId + " connecté à la partie " + gameId);

        // envoyer l'état actuel de la partie au joueur qui vient de se connecter
        sendGameState(gameId, playerId, session);
    }

    // un joueur envoie un message (action de jeu)
    // format attendu : {"action":"play","cardId":5,"chosenColor":"ROUGE"}
    // ou              : {"action":"draw"}
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        Long gameId = (Long) session.getUserProperties().get("gameId");
        Long playerId = (Long) session.getUserProperties().get("playerId");

        try {
            Map<String, Object> data = objectMapper.readValue(message, Map.class);
            String action = data.get("action").toString();
            Player player = playerRepo.findById(playerId).orElseThrow();

            if (action.equals("play")) {
                Long cardId = Long.valueOf(data.get("cardId").toString());
                String chosenColor = data.getOrDefault("chosenColor", "").toString();
                gameService.playCard(gameId, cardId, player, chosenColor);
            } else if (action.equals("draw")) {
                gameService.drawCard(gameId, player);
            }

            broadcastGameState(gameId);

        } catch (RuntimeException e) {
            // envoyer l'erreur uniquement au joueur qui a fait l'action
            String errorJson = "{\"error\":\"" + e.getMessage() + "\"}";
            session.getBasicRemote().sendText(errorJson);
        }
    }

    // un joueur se déconnecte
    @OnClose
    public void onClose(Session session) {
        Long gameId = (Long) session.getUserProperties().get("gameId");
        Long playerId = (Long) session.getUserProperties().get("playerId");

        // retirer la session de la map
        if (gameSessions.containsKey(gameId)) {
            gameSessions.get(gameId).remove(session);
        }

        System.out.println("Joueur " + playerId + " déconnecté de la partie " + gameId);
    }

    // erreur WebSocket
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Erreur WebSocket : " + throwable.getMessage());
    }

    // envoyer l'état de la partie à tous les joueurs connectés
    private void broadcastGameState(Long gameId) throws IOException {
        Set<Session> sessions = gameSessions.get(gameId);
        if (sessions == null) return;

        for (Session s : sessions) {
            if (s.isOpen()) {
                Long playerId = (Long) s.getUserProperties().get("playerId");
                sendGameState(gameId, playerId, s);
            }
        }
    }

    // envoyer l'état de la partie à un joueur spécifique
    private void sendGameState(Long gameId, Long playerId, Session session)
            throws IOException {
        try {
            // construire le GameStateDTO pour ce joueur
            GameStateDTO dto = gameService.getGameState(gameId, playerId);
            String json = objectMapper.writeValueAsString(dto);
            session.getBasicRemote().sendText(json);
        } catch (Exception e) {
            System.err.println("Erreur envoi état : " + e.getMessage());
        }
    }
}