package blazing.jeux.entity;

import jakarta.persistence.*;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;   // ROUGE, BLEU, VERT, JAUNE, NOIR
    private String value;   // 0-9, PASSER, INVERSE, PLUS2, PLUS4, JOKER

    private boolean discard; // true si c'est la carte visible sur la défausse

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @ManyToOne
    @JoinColumn(name = "holder_id")
    private GamePlayer holder; // null si dans la pioche

    public Card() {}

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getColor() { 
        return color; 
    }

    public void setColor(String color) { 
        this.color = color; 
    }

    public String getValue() { 
        return value; 
    }

    public void setValue(String value) { 
        this.value = value; 
    }

    public boolean isDiscard() { 
        return discard; 
    }

    public void setDiscard(boolean discard) { 
        this.discard = discard; 
    }

    public Deck getDeck() { 
        return deck; 
    }

    public void setDeck(Deck deck) { 
        this.deck = deck; 
    }

    public GamePlayer getHolder() { 
        return holder; 
    }

    public void setHolder(GamePlayer holder) { 
        this.holder = holder; 
    }
}