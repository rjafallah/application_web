package com.cartes.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GameTurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card; // null si le joueur a pioché

    public GameTurn() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public Game getGame() { 
        return game; 
    }

    public void setGame(Game game) { 
        this.game = game; 
    }

    public Player getPlayer() { 
        return player; 
    }

    public void setPlayer(Player player) { 
        this.player = player; 
    }

    public Card getCard() { 
        return card; 
    }

    public void setCard(Card card) { 
        this.card = card; 
    }
}