package blazing.jeux.controller;

import blazing.jeux.dto.LobbyGameDTO;
import blazing.jeux.entity.Game;
import blazing.jeux.repositories.GamePlayerRepo;
import blazing.jeux.repositories.PlayerRepo;
import blazing.jeux.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private GamePlayerRepo gamePlayerRepo;

    // lister les parties en attente
    @GetMapping
    public List<LobbyGameDTO> getWaitingGames() {
        List<Game> games = gameService.getWaitingGames();
        List<LobbyGameDTO> result = new ArrayList<>();
        for (Game game : games) {
            int count = gamePlayerRepo.findByGame(game).size();
            result.add(new LobbyGameDTO(game, count));
        }
        return result;
    }

    // créer une partie
    @PostMapping("/create")
    public Long createGame(@RequestParam Long playerId) {
        Game game = gameService.createGame(playerRepo.findById(playerId).orElseThrow(() -> new RuntimeException("Joueur introuvable")));
        return game.getId();
    }

    // rejoindre une partie
    @PostMapping("/join/{gameId}")
    public Long joinGame(@PathVariable Long gameId, @RequestParam Long playerId) {
        Game game = gameService.joinGame(gameId, playerRepo.findById(playerId).orElseThrow(() -> new RuntimeException("Joueur introuvable")));
        return game.getId();
    }
}