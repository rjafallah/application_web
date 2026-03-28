package com.cartes.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Player sender;

    public Message() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getContent() { 
        return content; 
    }

    public void setContent(String content) { 
        this.content = content; 
    }

    public LocalDateTime getSentAt() { 
        return sentAt; 
    }

    public void setSentAt(LocalDateTime sentAt) { 
        this.sentAt = sentAt; 
    }

    public Game getGame() { 
        return game; 
    }

    public void setGame(Game game) { 
        this.game = game; 
    }

    public Player getSender() { 
        return sender; 
    }

    public void setSender(Player sender) { 
        this.sender = sender; 
    }
}