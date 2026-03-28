package com.cartes.entity;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;      // WAITING, IN_PROGRESS, FINISHED
    private String direction;   // CLOCKWISE, COUNTERCLOCKWISE
    private String currentColor; // couleur active sur la défausse

    @ManyToOne
    @JoinColumn(name = "current_player_id")
    private Player currentPlayer;

    @OneToMany(mappedBy = "game")
    private Collection<GamePlayer> gamePlayers;

    @OneToOne(mappedBy = "game")
    private Deck deck;

    @OneToMany(mappedBy = "game")
    private Collection<Message> messages;

    @OneToMany(mappedBy = "game")
    private Collection<GameTurn> turns;

    public Game() {}

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

    public String getDirection() { 
        return direction; 
    }

    public void setDirection(String direction) { 
        this.direction = direction; 
    }

    public String getCurrentColor() { 
        return currentColor; 
    }

    public void setCurrentColor(String currentColor) { 
        this.currentColor = currentColor; 
    }

    public Player getCurrentPlayer() { 
        return currentPlayer; 
    }

    public void setCurrentPlayer(Player currentPlayer) { 
        this.currentPlayer = currentPlayer; 
    }

    public Collection<GamePlayer> getGamePlayers() { 
        return gamePlayers; 
    }

    public void setGamePlayers(Collection<GamePlayer> gamePlayers) { 
        this.gamePlayers = gamePlayers; 
    }

    public Deck getDeck() { 
        return deck; 
    }

    public void setDeck(Deck deck) { 
        this.deck = deck; 
    }

    public Collection<Message> getMessages() { 
        return messages; 
    }

    public void setMessages(Collection<Message> messages) { 
        this.messages = messages; 
    }

    public Collection<GameTurn> getTurns() { 
        return turns; 
    }

    public void setTurns(Collection<GameTurn> turns) { 
        this.turns = turns; 
    }
}