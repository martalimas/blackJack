package org.academiadecodigo.powrangers;

import org.academiadecodigo.powrangers.Card;
import org.academiadecodigo.powrangers.CardNype;

public class Deck {

    private Card[] inicialDeck;


    public Deck(){

        resetDeck();

    }

    public Card getCard() {

        Card cardToGive = null;

        while (cardToGive == null) {

            int cardN = (int) (Math.random() * 51);
            cardToGive = inicialDeck[cardN];
            inicialDeck[cardN] = null;
    }

        return cardToGive;
    }

    public int getSize() {
        int x = 0;
        for (Card card : inicialDeck) {
            if (card != null) x++;
        }
        return x;
    }

    public void resetDeck(){

        inicialDeck = new Card[]{

                new Card("2", CardNype.HEARTS),
                new Card("3",CardNype.HEARTS),
                new Card("4",CardNype.HEARTS),
                new Card("5",CardNype.HEARTS),
                new Card("6",CardNype.HEARTS),
                new Card("7",CardNype.HEARTS),
                new Card("8",CardNype.HEARTS),
                new Card("9",CardNype.HEARTS),
                new Card("10",CardNype.HEARTS),
                new Card("Jack",CardNype.HEARTS),
                new Card("Queen",CardNype.HEARTS),
                new Card("King",CardNype.HEARTS),
                new Card("Ace",CardNype.HEARTS),

                new Card("2",CardNype.CLUBS),
                new Card("3",CardNype.CLUBS),
                new Card("4",CardNype.CLUBS),
                new Card("5",CardNype.CLUBS),
                new Card("6",CardNype.CLUBS),
                new Card("7",CardNype.CLUBS),
                new Card("8",CardNype.CLUBS),
                new Card("9",CardNype.CLUBS),
                new Card("10",CardNype.CLUBS),
                new Card("Jack",CardNype.CLUBS),
                new Card("Queen",CardNype.CLUBS),
                new Card("King",CardNype.CLUBS),
                new Card("Ace",CardNype.CLUBS),

                new Card("2",CardNype.DIAMONDS),
                new Card("3",CardNype.DIAMONDS),
                new Card("4",CardNype.DIAMONDS),
                new Card("5",CardNype.DIAMONDS),
                new Card("6",CardNype.DIAMONDS),
                new Card("7",CardNype.DIAMONDS),
                new Card("8",CardNype.DIAMONDS),
                new Card("9",CardNype.DIAMONDS),
                new Card("10",CardNype.DIAMONDS),
                new Card("Jack",CardNype.DIAMONDS),
                new Card("Queen",CardNype.DIAMONDS),
                new Card("King",CardNype.DIAMONDS),
                new Card("Ace",CardNype.DIAMONDS),

                new Card("2",CardNype.SPADES),
                new Card("3",CardNype.SPADES),
                new Card("4",CardNype.SPADES),
                new Card("5",CardNype.SPADES),
                new Card("6",CardNype.SPADES),
                new Card("7",CardNype.SPADES),
                new Card("8",CardNype.SPADES),
                new Card("9",CardNype.SPADES),
                new Card("10",CardNype.SPADES),
                new Card("Jack",CardNype.SPADES),
                new Card("Queen",CardNype.SPADES),
                new Card("King",CardNype.SPADES),
                new Card("Ace",CardNype.SPADES)
        };
    }
}








