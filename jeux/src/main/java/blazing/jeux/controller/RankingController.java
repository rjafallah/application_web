// package blazing.jeux.controller;

// import blazing.jeux.entity.Ranking;
// import blazing.jeux.service.RankingService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/ranking")
// public class RankingController {

//     @Autowired
//     private RankingService rankingService;

//     @GetMapping
//     public ResponseEntity<List<Ranking>> getRanking() {
//         return ResponseEntity.ok(rankingService.getFullRanking());
//     }
// }
