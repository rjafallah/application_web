package blazing.jeux.controller;

import blazing.jeux.dto.LoginRequest;
import blazing.jeux.dto.PlayerDTO;
import blazing.jeux.dto.RegisterRequest;
import blazing.jeux.entity.Player;
import blazing.jeux.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public PlayerDTO register(@RequestBody RegisterRequest request) {
        Player player = authService.register(request.getUsername(), request.getPassword());
        return new PlayerDTO(player);
    }

    @PostMapping("/login")
    public PlayerDTO login(@RequestBody LoginRequest request) {
        Player player = authService.login(request.getUsername(), request.getPassword());
        return new PlayerDTO(player);
    }
}