package blazing.jeux.entity;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "deck")
    private Collection<Card> cards;

    public Deck() {}

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

    public Collection<Card> getCards() { 
        return cards; 
    }

    public void setCards(Collection<Card> cards) { 
        this.cards = cards; 
    }
}