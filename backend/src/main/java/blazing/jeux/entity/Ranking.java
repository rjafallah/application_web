package blazing.jeux.entity;

import jakarta.persistence.*;

@Entity
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalGames;
    private int totalWins;
    private int totalLosses;

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public Ranking() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public int getTotalGames() { 
        return totalGames; 
    }

    public void setTotalGames(int totalGames) { 
        this.totalGames = totalGames; 
    }

    public int getTotalWins() { 
        return totalWins; 
    }

    public void setTotalWins(int totalWins) { 
        this.totalWins = totalWins; 
    }

    public int getTotalLosses() { 
        return totalLosses; 
    }

    public void setTotalLosses(int totalLosses) { 
        this.totalLosses = totalLosses; 
    }

    public Player getPlayer() { 
        return player; 
    }

    public void setPlayer(Player player) { 
        this.player = player; 
    }
}