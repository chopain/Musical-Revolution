package Revolution;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import objects.Propaganda;
import people.plebian;
import people.propagandist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.TreeMap;

import static Revolution.ClassType.*;
import static Revolution.MoveType.*;

public class CommemeismGateway {

    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private WorldPane world;

    // Establish the connection to the server.
    public CommemeismGateway(WorldPane world) {
        this.world = world;
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an output stream to send data to the server
            outputToServer = new PrintWriter(socket.getOutputStream());

            // Create an input stream to read data from the server
            inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // Start the chat by sending in the user's handle.
    public synchronized void sendHandle(String handle) {
        outputToServer.println(SEND_HANDLE);
        outputToServer.println(handle);
        outputToServer.flush();
    }

    // Start the chat by sending in the user's handle.
    public synchronized void sendClass(String type) {
        outputToServer.println(SEND_CLASS);
        outputToServer.println(type);
        outputToServer.flush();
    }


    // Update player move
    public synchronized void move(int move) {
        outputToServer.println(SEND_MOVE);
        outputToServer.println(move);
        outputToServer.flush();
    }

    public synchronized int getMoveCount() {
        outputToServer.println(GET_MOVE_COUNT);
        outputToServer.flush();
        int count = 0;
        try {
            count = Integer.parseInt(inputFromServer.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    //Get updated position
    public synchronized void getMoves(int n, TreeMap<String, propagandist> player) throws IOException {
        outputToServer.println(GET_MOVE);
        outputToServer.println(n);
        outputToServer.flush();
        String user = inputFromServer.readLine();
        double newX = Double.parseDouble(inputFromServer.readLine());
        double newY = Double.parseDouble(inputFromServer.readLine());
        player.get(user).move(newX, newY);
    }


    public synchronized ObservableList<String> getClassTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("Bourgeois");
        //types.add("Proletariat");
        types.add("Communist");
        return types;
    }


    public synchronized int getPlayerCount() {
        outputToServer.println(GET_PLAYER_COUNT);
        outputToServer.flush();
        int count = 0;
        try {
            count = Integer.parseInt(inputFromServer.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public synchronized String getPlayerHandle(int n) throws IOException {
        outputToServer.println(GET_PLAYER);
        outputToServer.println(n);
        outputToServer.flush();
        String handle = inputFromServer.readLine();
        return handle;
    }

    public synchronized String getPlayerType(int n) throws IOException {
        outputToServer.println(GET_PLAYER_TYPE);
        outputToServer.println(n);
        outputToServer.flush();
        String type = inputFromServer.readLine();
        return type;
    }

    public synchronized void getPlayerPos(int n, propagandist p) throws IOException {
        outputToServer.println(GET_PLAYER_POSITION);
        outputToServer.println(n);
        outputToServer.flush();
        double newX = Double.parseDouble(inputFromServer.readLine());
        double newY = Double.parseDouble(inputFromServer.readLine());
        p.move(newX, newY);
    }


    public synchronized int getPlebianCount() {
        outputToServer.println(GET_PLEBIAN_COUNT);
        outputToServer.flush();
        int count = 0;
        try {
            count = Integer.parseInt(inputFromServer.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public synchronized void getPlebian(int n, List<plebian> plebs, List<ImageView> plebImages) throws IOException {
        outputToServer.println(GET_PLEBIAN);
        outputToServer.println(n);
        outputToServer.flush();
        plebs.add(n, new plebian());
        double newX = Double.parseDouble(inputFromServer.readLine());
        double newY = Double.parseDouble(inputFromServer.readLine());
        plebs.get(n).move(newX, newY);
        plebImages.add(plebs.get(n).getFace());
    }

    public synchronized void removePlebian(int n, List<plebian> plebs, List<ImageView> plebImages) throws IOException {
        outputToServer.println(GET_PLEBIAN);
        outputToServer.println(n);
        outputToServer.flush();
        plebs.add(n, new plebian());
        double newX = Double.parseDouble(inputFromServer.readLine());
        double newY = Double.parseDouble(inputFromServer.readLine());
        plebs.get(n).move(newX, newY);
        plebImages.add(plebs.get(n).getFace());
    }

    public synchronized int getPropagandaCount() {
        outputToServer.println(GET_PROPAGANDA_COUNT);
        outputToServer.flush();
        int count = 0;
        try {
            count = Integer.parseInt(inputFromServer.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public synchronized void getPropaganda(int n, List<Propaganda> propaganda, List<ImageView> props) throws IOException {
        outputToServer.println(GET_PROPAGANDA);
        outputToServer.println(n);
        outputToServer.flush();
        double newX = Double.parseDouble(inputFromServer.readLine());
        double newY = Double.parseDouble(inputFromServer.readLine());
        propaganda.add(n, new Propaganda(newX, newY));
        props.add(propaganda.get(n).getShape());
        System.out.println("get: "+props);
    }

    public synchronized void getPropagandaPos(int n, Propaganda p) throws IOException {
        outputToServer.println(GET_PROPAGANDA_POSITION);
        outputToServer.println(n);
        outputToServer.flush();
        double newX = Double.parseDouble(inputFromServer.readLine());
        double newY = Double.parseDouble(inputFromServer.readLine());
        p.updateShape(newX, newY);
    }

    public synchronized void removePropaganda(int n, List<plebian> plebs, List<ImageView> plebImages) throws IOException {
        outputToServer.println();
        outputToServer.println(n);
        outputToServer.flush();
        plebs.add(n, new plebian());
        double newX = Double.parseDouble(inputFromServer.readLine());
        double newY = Double.parseDouble(inputFromServer.readLine());
        plebs.get(n).move(newX, newY);
        plebImages.add(plebs.get(n).getFace());
    }


    public synchronized void throwObject() {
        outputToServer.println(THROW);
        outputToServer.flush();
    }

    public synchronized void checkScore(ScorePane scorePane) throws IOException {
        outputToServer.println(GET_SCORE);
        outputToServer.flush();
        double c = Double.parseDouble(inputFromServer.readLine());
        double b = Double.parseDouble(inputFromServer.readLine());
        double p = Double.parseDouble(inputFromServer.readLine());
        scorePane.setScores(c, b, p);
    }
}
