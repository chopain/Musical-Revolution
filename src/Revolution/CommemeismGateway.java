package Revolution;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import static Revolution.ClassType.SEND_HANDLE;
import static Revolution.ClassType.SEND_MOVE;

public class CommemeismGateway {

    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private WorldPane world;
    private ReentrantLock lock = new ReentrantLock();
    private static int newCommentsCount = 0;
    private int curUserComments = 0;

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

        }

    }


    // Start the chat by sending in the user's handle.
    public void sendHandle(String handle) {
        lock.lock();
        outputToServer.println();
        outputToServer.println(handle);
        outputToServer.flush();
        lock.unlock();
    }

    // Start the chat by sending in the user's handle.
    public void sendClass(String type) {
        lock.lock();
        outputToServer.println(SEND_HANDLE);
        outputToServer.println(type);
        outputToServer.flush();
        lock.unlock();
    }


    // Update player move
    public void move(int move) {
        lock.lock();
        outputToServer.println(SEND_MOVE);
        outputToServer.println(move);
        outputToServer.flush();
        lock.unlock();
    }

    //Get list of players
    public ObservableList<String> getPlayers() {
        lock.lock();
        ObservableList<String> rooms = FXCollections.observableArrayList();
        outputToServer.println();
        outputToServer.flush();
        try {
            int roomNums = Integer.parseInt(inputFromServer.readLine());
            for (int i = 0; i < roomNums; i++) {
                rooms.add(inputFromServer.readLine());
            }
        } catch (IOException ex) {
            //Platform.runLater(() -> textArea.appendText("Error in getComment: " + ex.toString() + "\n"));
        }
        lock.unlock();
        return rooms;
    }




    public ObservableList<String> getClassTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("Bourgeois");
        types.add("Proletariat");
        types.add("Communist");
        return types;
    }
}
