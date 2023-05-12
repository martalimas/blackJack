package org.academiadecodigo.powrangers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BlackJack {

    private static final int PORT_NUMBER = 6678;
    private static final int MAX_PLAYERS = 3;


    private Dealer dealer;
    private ArrayList<Player> playersArrayList = new ArrayList<>();

    private ServerSocket serverSocket;
    private BufferedWriter out;
    private BufferedReader in;

    private boolean isGameRunning = false;
    private int connectionCounter = 0;

    private static int playersReady = 0;

    private static ExecutorService executorService = Executors.newFixedThreadPool((MAX_PLAYERS*2)+2);



    public void startServer(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server started!\nWe are running in port: " + portNumber);
            waitConnections();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        BlackJack blackJack = new BlackJack();

        if (args.length < 1) {
            System.out.println("We will use our default port: " + PORT_NUMBER + " .\nIf you want to use another port please type it like: java org.academiadecodigo.powrangers.BlackJack 9999");
            System.out.println("Ok....welcome to our blackjack game.... The game requires " + MAX_PLAYERS + " players for it to start!\nSo join quickly!");
            blackJack.startServer(PORT_NUMBER);
        }


    }

    public void startGame() {
        isGameRunning = true;
        executorService.submit(new Dealer(playersArrayList));

    }

    public void sendGoodByeWeAreFull(Socket connection) throws IOException {
        out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        out.write("The server's currently full. Please try again later");
        out.flush();

        out.close();
        connection.close();
    }


    public void waitConnections() {
        while (true) {
            try  {
                System.out.println("Trying to accept a new client....");
                Socket connection = serverSocket.accept();
                System.out.println("Client accepted....");
                if (isGameRunning) { // Game's currently ongoing, so tell the client to try again later
                    System.out.println("Client accepted but exceed. informing and closing socket");
                    sendGoodByeWeAreFull(connection);
                    continue;
                }
                Player player = new Player(connection);
                playersArrayList.add(player);
                connectionCounter++; // Increment this integer so we know when to start the game

                executorService.submit(new PlayerHandler(connection,player));


            } catch(IOException e) {
                e.printStackTrace();
            }

            if (connectionCounter == MAX_PLAYERS) {
               while (true) {
                   // Please help PLEASE HELPPPPPPPPPP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                   System.out.println("We have " + playersReady + " players ready!!..");
                   if (playersReady == MAX_PLAYERS) {
                       startGame();
                       break;
                   }
               }
            }

        }
    }

    class PlayerHandler implements Runnable {
        private ArrayList<PlayerHandler> playerHandlerArrayList = new ArrayList<>();
        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;
        private String name;
        private Player player;



        public PlayerHandler(Socket socket, Player player) throws IOException {
            this.socket = socket;
            this.player = player;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            playerHandlerArrayList.add(this);
        }


        private void getPlayerName() throws IOException {
            out.write("Please tell me ur name:\n");
            out.flush();
            this.name = in.readLine();
            Thread.currentThread().setName(name);
            player.setName(name);
        }

        @Override
        public void run() {
            try {
                getPlayerName();
                out.write("Welcome " + player.getName() + " to the org.academiadecodigo.powrangers.BlackJack!\n");
                out.write("We are currently waiting for all players to enter\n");
                out.write("For a better experience set your window size to its maximum size\n");
                out.flush();
                playersReady++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }



}





