package blazing.jeux.service;

import blazing.jeux.entity.Card;
import blazing.jeux.entity.Deck;
import blazing.jeux.entity.Game;
import blazing.jeux.entity.GamePlayer;
import blazing.jeux.repositories.CardRepo;
import blazing.jeux.repositories.DeckRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeckService {

    @Autowired
    private DeckRepo deckRepo;

    @Autowired
    private CardRepo cardRepo;

    private static final String[] COLORS = {"ROUGE", "BLEU", "VERT", "JAUNE"};
    private static final String[] VALUES = {"1", "2", "3", "4", "5", "6", "7", "9"};

    public Deck createDeck(Game game) {
        Deck deck = new Deck();
        deck.setGame(game);
        deckRepo.save(deck);

        List<Card> cards = new ArrayList<>();

        // 32 cartes normales
        for (String color : COLORS) {
            for (String value : VALUES) {
                Card c = new Card();
                c.setColor(color);
                c.setValue(value);
                c.setDeck(deck);
                c.setDiscard(false);
                cards.add(c);
            }
        }

        // 4 Wild 8 : 1 par couleur, jouable à tout moment
        for (String color : COLORS) {
            Card c = new Card();
            c.setColor(color);
            c.setValue("8");
            c.setDeck(deck);
            c.setDiscard(false);
            cards.add(c);
        }

        // 4 Oscar's Swap : sans couleur, jouable à tout moment
        for (int i = 0; i < 4; i++) {
            Card c = new Card();
            c.setColor("NONE");
            c.setValue("OSCARS_SWAP");
            c.setDeck(deck);
            c.setDiscard(false);
            cards.add(c);
        }

        // 4 Giddy Up : 1 par couleur, bloque le joueur suivant
        for (String color : COLORS) {
            Card c = new Card();
            c.setColor(color);
            c.setValue("GIDDY_UP");
            c.setDeck(deck);
            c.setDiscard(false);
            cards.add(c);
        }

        // 4 Wrong Way : 1 par couleur, inverse le sens du jeu
        for (String color : COLORS) {
            Card c = new Card();
            c.setColor(color);
            c.setValue("WRONG_WAY");
            c.setDeck(deck);
            c.setDiscard(false);
            cards.add(c);
        }

        // mélanger et sauvegarder
        Collections.shuffle(cards);
        cardRepo.saveAll(cards);
        return deck;
    }

    // distribuer 5 cartes à chaque joueur et retourner la première carte
    public void distributeCards(Deck deck, List<GamePlayer> players) {
        List<Card> pioche = cardRepo.findPioche(deck);
        int index = 0;

        for (GamePlayer gp : players) {
            for (int i = 0; i < 5; i++) {
                Card card = pioche.get(index++);
                card.setHolder(gp);
                cardRepo.save(card);
            }
        }

        // première carte de la défausse ne doit pas être un 8 ou un OSCARS_SWAP
        Card firstDiscard = pioche.get(index);
        while (firstDiscard.getValue().equals("8")
                || firstDiscard.getValue().equals("OSCARS_SWAP")) {
            index++;
            firstDiscard = pioche.get(index);
        }
        firstDiscard.setDiscard(true);
        cardRepo.save(firstDiscard);
    }

    // piocher une carte depuis la pioche
    public Card drawCard(Deck deck, GamePlayer player) {
        List<Card> pioche = cardRepo.findPioche(deck);
        if (pioche.isEmpty()) {
            throw new RuntimeException("La pioche est vide");
        }
        Card card = pioche.get(0);
        card.setHolder(player);
        cardRepo.save(card);
        return card;
    }

    // compter les cartes restantes dans la pioche
    public int countPioche(Deck deck) {
        return cardRepo.findPioche(deck).size();
    }
}