package blazing.jeux.dto;

import blazing.jeux.entity.Ranking;

public class RankingDTO {
    private String username;
    private int totalWins;
    private int totalLosses;
    private int totalGames;

    public RankingDTO() {}

    public RankingDTO(Ranking ranking) {
        this.username = ranking.getPlayer().getUsername();
        this.totalWins = ranking.getTotalWins();
        this.totalLosses = ranking.getTotalLosses();
        this.totalGames = ranking.getTotalGames();
    }

    public String getUsername() { 
        return username; 
    }

    public void setUsername(String username) { 
        this.username = username; 
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

    public int getTotalGames() { 
        return totalGames; 
    }

    public void setTotalGames(int totalGames) { 
        this.totalGames = totalGames; 
    }
}