// package blazing.jeux.controller;

// import blazing.jeux.dto.LoginRequest;
// import blazing.jeux.dto.PlayerDTO;
// import blazing.jeux.dto.RegisterRequest;
// import blazing.jeux.entity.Player;
// import blazing.jeux.service.AuthService;
// import jakarta.servlet.http.HttpSession;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {

//     @Autowired
//     private AuthService authService;

//     @PostMapping("/register")
//     public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//         try {
//             Player player = authService.register(request.getUsername(), request.getPassword());
//             return ResponseEntity.ok(new PlayerDTO(player));
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @PostMapping("/login")
//     public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
//         try {
//             Player player = authService.login(request.getUsername(), request.getPassword());
//             session.setAttribute("player", player);
//             return ResponseEntity.ok(new PlayerDTO(player));
//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//     }

//     @PostMapping("/logout")
//     public ResponseEntity<?> logout(HttpSession session) {
//         session.invalidate();
//         return ResponseEntity.ok("Déconnecté");
//     }

//     @GetMapping("/me")
//     public ResponseEntity<?> me(HttpSession session) {
//         Player player = (Player) session.getAttribute("player");
//         if (player == null) {
//             return ResponseEntity.status(401).body("Non connecté");
//         }
//         return ResponseEntity.ok(new PlayerDTO(player));
//     }
// }
