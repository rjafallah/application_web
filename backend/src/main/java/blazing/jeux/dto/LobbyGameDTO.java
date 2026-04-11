package blazing.jeux.dto;

import blazing.jeux.entity.Game;

public class LobbyGameDTO {
    private Long id;
    private String status;
    private int playerCount;

    public LobbyGameDTO() {}

    public LobbyGameDTO(Game game, int playerCount) {
        this.id = game.getId();
        this.status = game.getStatus();
        this.playerCount = playerCount;
    }

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getStatus() {
        return status; 
    }

    public void setStatus(String status) { 
        this.status = status; 
    }

    public int getPlayerCount() { 
        return playerCount; 
    }

    public void setPlayerCount(int playerCount) { 
        this.playerCount = playerCount; 
    }
}