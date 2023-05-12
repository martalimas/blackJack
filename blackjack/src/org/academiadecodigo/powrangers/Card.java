package org.academiadecodigo.powrangers;

public class Card {

    private CardNype nype;
    private String cardName;
    private int value;

    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static final String RESET = "\033[0m";  // Text Reset

    public Card(String cardName, CardNype nype){

        this.cardName = cardName;
        this.nype = nype;

        switch (cardName){

            case "King":
                value = 10;
                break;
            case "Queen":
                value = 10;
                break;
            case "Ace":
                value = 1;
                break;
            case"Jack":
                value = 10;
                break;
            default:
                value = Integer.parseInt(cardName);
                break;

        }

    }

    public String getNype() {
        String symbol = "";

        switch (nype) {
            case CLUBS:
                symbol = RED + "♣" + RESET;
                break;
            case HEARTS:
                symbol = RED + "♥" + RESET;
                break;
            case SPADES:
                symbol = RED + "♠" + RESET;
                break;
            case DIAMONDS:
                symbol = RED + "♦" + RESET;
                break;

        }
        return symbol;
    }

    public String getCardName() {
        return cardName;
    }

    public int getValue() {
        return value;
    }
}
