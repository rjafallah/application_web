package blazing.jeux.websocket;

import blazing.jeux.entity.Game;
import blazing.jeux.entity.Message;
import blazing.jeux.entity.Player;
import blazing.jeux.repositories.GameRepo;
import blazing.jeux.repositories.MessageRepo;
import blazing.jeux.repositories.PlayerRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/chat/{gameId}/{playerId}")
public class ChatEndpoint {

    // stocke les sessions de chat par gameId
    private static Map<Long, Set<Session>> chatSessions = new ConcurrentHashMap<>();

    private static PlayerRepo playerRepo;
    private static GameRepo gameRepo;
    private static MessageRepo messageRepo;
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setPlayerRepo(PlayerRepo playerRepo) {
        ChatEndpoint.playerRepo = playerRepo;
    }

    @Autowired
    public void setGameRepo(GameRepo gameRepo) {
        ChatEndpoint.gameRepo = gameRepo;
    }

    @Autowired
    public void setMessageRepo(MessageRepo messageRepo) {
        ChatEndpoint.messageRepo = messageRepo;
    }

    // un joueur ouvre le chat
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("gameId") Long gameId,
                       @PathParam("playerId") Long playerId) {
        chatSessions.computeIfAbsent(gameId, k -> ConcurrentHashMap.newKeySet())
                .add(session);

        session.getUserProperties().put("gameId", gameId);
        session.getUserProperties().put("playerId", playerId);

        System.out.println("Joueur " + playerId + " connecté au chat " + gameId);
    }

    // un joueur envoie un message
    // format attendu : {"content":"Bonjour !"}
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        Long gameId = (Long) session.getUserProperties().get("gameId");
        Long playerId = (Long) session.getUserProperties().get("playerId");

        // parser le message
        Map<String, Object> data = objectMapper.readValue(message, Map.class);
        String content = data.get("content").toString();

        Player player = playerRepo.findById(playerId).orElseThrow();
        Game game = gameRepo.findById(gameId).orElseThrow();

        // sauvegarder le message en BDD
        Message msg = new Message();
        msg.setContent(content);
        msg.setSender(player);
        msg.setGame(game);
        msg.setSentAt(LocalDateTime.now());
        messageRepo.save(msg);

        // broadcaster à tous les joueurs du chat
        String response = objectMapper.writeValueAsString(Map.of(
                "sender", player.getUsername(),
                "content", content,
                "sentAt", msg.getSentAt().toString()
        ));

        broadcast(gameId, response);
    }

    // un joueur ferme le chat
    @OnClose
    public void onClose(Session session) {
        Long gameId = (Long) session.getUserProperties().get("gameId");
        if (chatSessions.containsKey(gameId)) {
            chatSessions.get(gameId).remove(session);
        }
    }

    // erreur
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Erreur chat WebSocket : " + throwable.getMessage());
    }

    // envoyer un message à tous les joueurs connectés au chat
    private void broadcast(Long gameId, String message) throws IOException {
        Set<Session> sessions = chatSessions.get(gameId);
        if (sessions == null) return;

        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(message);
            }
        }
    }
}