package org.academiadecodigo.powrangers;

import org.academiadecodigo.powrangers.Card;

import java.net.Socket;
import java.util.ArrayList;

public class Player{

    private ArrayList<Card> myCards = new ArrayList<>();
    private int myCardsValue;
    private Socket socket;
    private String name;
    private boolean isPassing = false;
    private boolean isOverBlackJack = false;


    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Player(Socket socket){
        this.socket = socket;
    }

    public Player(String name){  // just for org.academiadecodigo.powrangers.Dealer

        this.name = name;
    }

    public Socket getSocket() {

        return socket;
    }

    public int getCardsValue(){

        return myCardsValue;
    }

    public String printStatus(){

        StringBuilder status = new StringBuilder();

        for (int i = 0; i < myCards.size(); i++) {
            status.append(myCards.get(i).getCardName() + myCards.get(i).getNype());
        }
        return status.toString();

    }

    public String printStatusDealer(){

        StringBuilder status = new StringBuilder();
        status.append(myCards.get(0).getCardName() + myCards.get(0).getNype());

        return status.toString();

    }

    public int hitRequest(Card card){

        int tempValue = 0;
       myCards.add(card);

        for (int i = 0; i < myCards.size(); i++) {
            switch (myCards.get(i).getCardName()){
                case "Ace":
                    if((myCardsValue + 11) > 21){

                        tempValue += myCards.get(i).getValue();
                        break;

                    }else{
                        tempValue += 1;
                        break;
                    }
                default:
                    tempValue += myCards.get(i).getValue();
                    break;
            }
        }

        myCardsValue = tempValue;

        if (myCardsValue == 21) {
            System.out.println("BLACKJACKK!!!! " + myCardsValue);
            return 2;
        }
        else if (myCardsValue > 21) {
            System.out.println("You've exceed the limit! " + myCardsValue);
            setOverBlackJack();
            return 1;
        }

        return 0;
    }

    public void recountCards(){
        int temporaryValue;

        for (int i = 0; i < myCards.size(); i++) {

        }

    }

    public ArrayList<Card> getMyCards() {
        return myCards;
    }

    public boolean isPassing() {

        return isPassing;
    }

    public void setPassing() {

        isPassing = true;
    }

    public boolean isOverBlackJack() {
        return isOverBlackJack;
    }

    public void setOverBlackJack() {
        isOverBlackJack = true;
    }

    public void resetPlayer(){

        isPassing = false;
        isOverBlackJack = false;
        myCards = new ArrayList<>();
        myCardsValue = 0;
    }

}
