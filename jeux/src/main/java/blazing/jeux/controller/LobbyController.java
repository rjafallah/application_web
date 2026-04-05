// package blazing.jeux.controller;

// import blazing.jeux.entity.Game;
// import blazing.jeux.entity.Player;
// import blazing.jeux.service.GameService;
// import jakarta.servlet.http.HttpSession;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/lobby")
// public class LobbyController {

//     @Autowired
//     private GameService gameService;

//     @GetMapping
//     public ResponseEntity<?> getWaitingGames(HttpSession session) {
//         Player player = (Player) session.getAttribute("player");
//         if (player == null) return ResponseEntity.status(401).body("Non connecté");
//         List<Game> games = gameService.getWaitingGames();
//         return ResponseEntity.ok(games);
//     }

//     @PostMapping("/create")
//     public ResponseEntity<?> createGame(HttpSession session) {
//         Player player = (Player) session.getAttribute("player");
//         if (player == null) return ResponseEntity.status(401).body("Non connecté");
//         try {
//             Game game = gameService.createGame(player);
//             return ResponseEntity.ok(game.getId());
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @PostMapping("/join/{gameId}")
//     public ResponseEntity<?> joinGame(@PathVariable Long gameId, HttpSession session) {
//         Player player = (Player) session.getAttribute("player");
//         if (player == null) return ResponseEntity.status(401).body("Non connecté");
//         try {
//             Game game = gameService.joinGame(gameId, player);
//             return ResponseEntity.ok(game.getId());
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }
// }
