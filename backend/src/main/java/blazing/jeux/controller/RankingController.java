package blazing.jeux.controller;

import blazing.jeux.dto.RankingDTO;
import blazing.jeux.entity.Ranking;
import blazing.jeux.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    public List<RankingDTO> getRanking() {
        List<Ranking> rankings = rankingService.getFullRanking();
        List<RankingDTO> result = new ArrayList<>();
        for (Ranking r : rankings) {
            result.add(new RankingDTO(r));
        }
        return result;
    }
}