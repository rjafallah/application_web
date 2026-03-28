package com.cartes.entity;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String passwordHash;

    @OneToMany(mappedBy = "player")
    private Collection<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "sender")
    private Collection<Message> messages;

    @OneToOne(mappedBy = "player")
    private Ranking ranking;

    public Player() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getUsername() { 
        return username; 
    }

    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPasswordHash() { 
        return passwordHash; 
    }

    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }

    public Collection<GamePlayer> getGamePlayers() { 
        return gamePlayers; 
    }

    public void setGamePlayers(Collection<GamePlayer> gamePlayers) { 
        this.gamePlayers = gamePlayers; 
    }

    public Collection<Message> getMessages() { 
        return messages; 
    }

    public void setMessages(Collection<Message> messages) { 
        this.messages = messages; 
    }

    public Ranking getRanking() { 
        return ranking; 
    }

    public void setRanking(Ranking ranking) { 
        this.ranking = ranking; 
    }
}