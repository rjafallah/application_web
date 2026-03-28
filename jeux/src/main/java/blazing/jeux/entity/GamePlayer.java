package com.cartes.entity;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "holder")
    private Collection<Card> hand;

    public GamePlayer() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public Player getPlayer() { 
        return player; 
    }

    public void setPlayer(Player player) { 
        this.player = player; 
    }

    public Game getGame() { 
        return game; 
    }

    public void setGame(Game game) { 
        this.game = game; 
    }

    public Collection<Card> getHand() { 
        return hand; 
    }

    public void setHand(Collection<Card> hand) { 
        this.hand = hand; 
    }
}