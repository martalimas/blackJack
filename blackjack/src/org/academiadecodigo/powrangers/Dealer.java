package org.academiadecodigo.powrangers;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Dealer extends Player implements Runnable {

    private Deck deck;
    private ArrayList<Player> players;

    private BufferedWriter out;


    private int playersOut;

    public Dealer(ArrayList<Player> players) {
        super("null");
        this.deck = new Deck();
        this.players = players;
    }


    public String organizeData() {
        StringBuilder temp = new StringBuilder();
        temp.append("***************************§***************************\n");
        temp.append("org.academiadecodigo.powrangers.Player Name\tCards\tPlayerScore\n");
        temp.append("DEALER \t" + ((getMyCards().size() > 2) ? printStatus() : printStatusDealer()) + "\t" + ((getMyCards().size() > 2) ? getCardsValue() : getMyCards().get(0).getValue()) + "\n");
        for (int i = 0 ; i < players.size() ; i++) {
            temp.append(players.get(i).getName() + "\t" + players.get(i).printStatus() + "\t" + players.get(i).getCardsValue() + " *§* \n");
        }
        temp.append("\n***************************§***************************\n");
        return temp.toString();
    }


    public void sendStatus() throws IOException {
        //Send 1 card from dealer to everyone
        for (int i = 0; i < players.size() ; i++) {
            out = new BufferedWriter(new OutputStreamWriter(players.get(i).getSocket().getOutputStream()));
            out.write(organizeData());
            out.flush();
        }
    }

    public void sendToAll(int option,Player player) throws IOException {
        String message = "";
        if (option == 1) {
            message = " just asked for another card\n";
        }
        if (option == 2) {
            message = " has just passed its turn!\n";
        }
        if (option == 3) {
            message = " has exceeded 21, so is out!\n";
        }

        for (int i = 0; i < players.size() ; i++) {
            out = new BufferedWriter(new OutputStreamWriter(players.get(i).getSocket().getOutputStream()));
            out.write(player.getName() + message);
            out.flush();
        }
    }

    public void sendMessageToAll(String message) throws IOException {
        for (int i = 0; i < players.size() ; i++) {
            out = new BufferedWriter(new OutputStreamWriter(players.get(i).getSocket().getOutputStream()));
            out.write(message);
            out.flush();
        }
    }



    public void startGame() throws IOException {

        // Still we have to show one of Dealer's card!

        this.hitRequest(deck.getCard());

        for (Player player : players) {      // all players get one cards
            player.hitRequest(deck.getCard());
        }

        this.hitRequest(deck.getCard());

        for (Player player : players) {      // all players get one cards
            player.hitRequest(deck.getCard());
        }

        sendStatus();

    }

    public void setToPass(Socket playerSocket){

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getSocket().equals(playerSocket)){
                if(players.get(i).isOverBlackJack() || players.get(i).isPassing()){
                    System.out.println("You are already out of this round, sorry");
                    break;
                }else {
                    players.get(i).setPassing();
                    playersOut++;
                    break;
                }
            }
        }

    }

    public void hit(Socket playerSocket) throws IOException {

        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getSocket().equals(playerSocket)){

                players.get(i).hitRequest(deck.getCard());

                sendToAll(1, players.get(i));

                if (players.get(i).isOverBlackJack()) {
                    sendToAll(3,players.get(i));
                }
                break;
            }
        }

    }


    @Override
    public void run() {
        while (true) {

            try {
                startGame();
                System.out.println("oi");
                Thread.sleep(2000);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }

            int internalPlayersOut = 0;

            while (internalPlayersOut < players.size()) {
                System.out.println("Players out: " + internalPlayersOut + " and players size: " + players.size());

                for (int i = 0; i < players.size(); i++) { //Dealer asks every player what does he want to do
                    while (true) {

                        if (players.get(i).isPassing() || players.get(i).getCardsValue() > 21) {
                            internalPlayersOut++;
                            System.out.println("You already lost or passed this round");
                            break;
                        }

                        try {
                            PrintStream printStream = new PrintStream(players.get(i).getSocket().getOutputStream());
                            Prompt prompt = new Prompt(players.get(i).getSocket().getInputStream(), printStream);

                            String[] options = {"HIT", "PASS"};
                            MenuInputScanner scanner = new MenuInputScanner(options);

                            scanner.setMessage("Would you like to HIT or PASS?");

                            int answerIndex = prompt.getUserInput(scanner);

                            switch ((answerIndex)) {
                                case 1:
                                    hit(players.get(i).getSocket());
                                    break;
                                case 2:
                                    setToPass(players.get(i).getSocket());
                                    sendToAll(2, players.get(i));
                                    break;
                            }
                            if (players.get(i).isOverBlackJack() || players.get(i).isPassing() || players.get(i).getCardsValue() > 21) {
                                internalPlayersOut++;
                                sendStatus();
                                break;
                            }
                            sendStatus();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }



            Player tmpX = new Player("null");

            for (Player player : players) {
                if ((tmpX.getCardsValue() < player.getCardsValue())  &&  (!player.isOverBlackJack())) {
                    tmpX = player;
                }
            }

            System.out.println("with old tecnic is: ");
            System.out.println("The max value that i've got is: " + tmpX.getCardsValue() + " and is from the player: " + tmpX.getName());


            // Let's check if there are still players able to play
            int playersCount = 0;
            for (Player player : players) {
                if (player.isOverBlackJack()) {
                    System.out.println(player.getName() + " has exceeded and will be counted out");
                    playersCount++;
                    System.out.println("Count: " + playersCount);
                }
            }
            System.out.println("My player count is: " + playersCount + " and my players.size is: " + players.size());
            if (playersCount == players.size()) {
                try {
                    sendStatus();
                    sendMessageToAll("All players have exceed 21 so the dealer wins.\n");
                    Thread.sleep(2000);
                    sendMessageToAll("THIS GAME WILL START OVER IN 5 seconds... Get ready for a new round\n");
                    Thread.sleep(5000);
                    resetGame();
                     continue;

                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

                while (getCardsValue() <= 17) {
                    hitRequest(deck.getCard());
                    try {
                        sendMessageToAll("DEALER DRAWS ANOTHER CARD\n");
                        sendStatus();
                        Thread.sleep(2000);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                // No more cards, lets evaluate ! :

                try {
                    sendMessageToAll("DEALER STOPPED DRAWING MORE CARDS\n");
                    Thread.sleep(2000);
                    sendMessageToAll("TIME TO FIND THE WINNER\n");
                    Thread.sleep(2000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //WINNER FINDER------------------------------------------------------------------------------

                //Let's remove exceed players


                ArrayList<Player> temporaryArray = new ArrayList<>(players);


                for (int i = 0; i < temporaryArray.size(); i++) { // Removes all players that lost out of the temporary array.
                    if (temporaryArray.get(i).getCardsValue() > 21 || temporaryArray.get(i).isOverBlackJack()) {
                        try {
                            sendMessageToAll("Dealer: " + temporaryArray.get(i).getName() + " has lost and will not be evaluated\n");
                            temporaryArray.remove(i);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            System.out.println("loading...");

                //Evaluates
                if (getCardsValue() > 21) {
                    try {
                        sendStatus();
                        sendMessageToAll("Dealer has exceed the value so he lost.\nThe winners are: \n");
                        StringBuilder list = new StringBuilder();
                        for (Player player : temporaryArray) {
                            list.append("Name: " + player.getName() + " Points: " + player.getCardsValue() + " Cards: " + player.printStatus() + "\n");
                        }
                        sendMessageToAll(list.toString());
                        Thread.sleep(2000);
                        resetGame();

                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else {

                    // Let's remove who has fewer points than dealer
                    for (Player player : temporaryArray) {
                        if (player.getCardsValue() < getCardsValue()) {
                            temporaryArray.remove(player);
                        }
                    }
                    System.out.println("About to announce winners!\n");
                    announceWinners(temporaryArray);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    resetGame();
                }

            }
    }
    //----------------------------------------------------------------------------------------------------------------------------------



    public void announceWinners(ArrayList<Player> players) {
        System.out.println("Announcing winners:\n");

        try {
            sendMessageToAll("The winners are:\n");
            for (Player player : players) {
                sendMessageToAll("Name: " + player.getName() + " Points: " + player.getCardsValue() + " Cards: " + player.printStatus() + "\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void resetGame(){
        deck.resetDeck();
        resetPlayer();
        try {
            sendMessageToAll("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Player player: players) {
            player.resetPlayer();
        }

    }



}

