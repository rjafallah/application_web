// package blazing.jeux.controller;

// import blazing.jeux.entity.Card;
// import blazing.jeux.entity.Game;
// import blazing.jeux.entity.Player;
// import blazing.jeux.service.GameService;
// import jakarta.servlet.http.HttpSession;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.Map;

// @RestController
// @RequestMapping("/api/game")
// public class GameController {

//     @Autowired
//     private GameService gameService;

//     @PostMapping("/{gameId}/start")
//     public ResponseEntity<?> startGame(@PathVariable Long gameId, HttpSession session) {
//         Player player = (Player) session.getAttribute("player");
//         if (player == null) return ResponseEntity.status(401).body("Non connecté");
//         try {
//             Game game = gameService.startGame(gameId);
//             return ResponseEntity.ok(game.getId());
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @GetMapping("/{gameId}")
//     public ResponseEntity<?> getGame(@PathVariable Long gameId, HttpSession session) {
//         Player player = (Player) session.getAttribute("player");
//         if (player == null) return ResponseEntity.status(401).body("Non connecté");
//         return gameService.getGame(gameId)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping("/{gameId}/play")
//     public ResponseEntity<?> playCard(
//             @PathVariable Long gameId,
//             @RequestBody Map<String, Object> body,
//             HttpSession session) {
//         Player player = (Player) session.getAttribute("player");
//         if (player == null) return ResponseEntity.status(401).body("Non connecté");
//         try {
//             Long cardId = Long.valueOf(body.get("cardId").toString());
//             String chosenColor = body.getOrDefault("chosenColor", "").toString();
//             Game game = gameService.playCard(gameId, cardId, player, chosenColor);
//             return ResponseEntity.ok(game.getStatus());
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @PostMapping("/{gameId}/draw")
//     public ResponseEntity<?> drawCard(@PathVariable Long gameId, HttpSession session) {
//         Player player = (Player) session.getAttribute("player");
//         if (player == null) return ResponseEntity.status(401).body("Non connecté");
//         try {
//             Card card = gameService.drawCard(gameId, player);
//             return ResponseEntity.ok(card.getId());
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }
// }
