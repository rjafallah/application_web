package blazing.jeux;

import blazing.jeux.entity.Card;
import blazing.jeux.entity.Game;
import blazing.jeux.entity.GamePlayer;
import blazing.jeux.entity.Player;
import blazing.jeux.entity.Deck;
import blazing.jeux.repositories.CardRepo;
import blazing.jeux.repositories.GamePlayerRepo;
import blazing.jeux.repositories.GameRepo;
import blazing.jeux.repositories.DeckRepo;
import blazing.jeux.service.AuthService;
import blazing.jeux.service.GameService;
import blazing.jeux.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import java.util.List;



@SpringBootApplication
public class JeuxApplication {

	public static void main(String[] args) {
		SpringApplication.run(JeuxApplication.class, args);
	}
}
