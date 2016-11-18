package Revolution;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import objects.Box;
import objects.Propaganda;
import people.plebian;
import people.propagandist;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static Revolution.MoveType.*;

public class CommemeismGateway extends Thread implements MessageCodes {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final GatewayListener listener;


    // Establish the connection to the server.
    public CommemeismGateway(GatewayListener listen) throws Exception {

        // Create a socket to connect to the server
        Socket socket = new Socket("143.44.75.183", 8000);


        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        listener = listen;
    }

    public void setFields(String name, int side) throws IOException {
        out.writeUTF(name);
        out.writeInt(side);

        listener.onInitialized(in.readInt(), in.readInt(), in.readInt(), in.readInt(),
                new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt()));

        Box[] walls = new Box[in.readInt()];
        for (int i = 0; i < walls.length; i++) {
            walls[i] = new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt());
        }
        Box[] voters = new Box[in.readInt()];
        for (int i = 0; i < voters.length; i++) {
            int id = in.readInt();
            voters[i] = new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt());
            voters[i]._id = id;
        }

        listener.onBoxesSet(voters, walls);

        int players = in.readInt();
        for (int i = 0; i < players; i++)
            listener.onClientChange(in.readInt(), in.readInt(), in.readUTF(), in.readInt(), in.readInt());
    }


    public interface GatewayListener {
        void onInitialized(int width, int height, int score0, int score1, Box me);

        void onBoxesSet(Box[] voters, Box[] walls);

        void onBallChange(int id, int ox, int oy);

        void onBoxChange(int id, int x, int y);

        void onClientChange(int id, int party, String name, int x, int y);

        void onScoreChange(int team, int score);

        void onBallRemove(int id);

        void onClientRemove(int id);

        void onError(Exception e);
    }

    private boolean error = false;

    private synchronized void throwError(Exception e) {
        if (error)
            return;
        error = true;
        listener.onError(e);
    }

    @Override
    public void run() {
        try {
            while (true) {
                boolean changed = in.readBoolean();
                int msg = in.readInt();
                switch (msg) {
                    case SERVER_CHANGED_BALL: {
                        int id = in.readInt();
                        int originX = in.readInt();
                        int originY = in.readInt();
                        if (changed)
                            Platform.runLater(() -> listener.onBallChange(id, originX, originY));
                        else
                            Platform.runLater(() -> listener.onBallRemove(id));
                        break;
                    }
                    case SERVER_CHANGED_CLIENT: {
                        int id = in.readInt();
                        int party = in.readInt();
                        String name = in.readUTF();
                        int originX = in.readInt();
                        int originY = in.readInt();
                        if (changed)
                            Platform.runLater(() -> listener.onClientChange(id, party, name, originX, originY));
                        else
                            Platform.runLater(() -> listener.onClientRemove(id));
                        break;
                    }
                    case SERVER_CHANGED_SCORE: {
                        int team = in.readInt();
                        int score = in.readInt();
                        Platform.runLater(() -> listener.onScoreChange(team, score));
                        break;
                    }
                    case SERVER_CHANGED_BOX: {
                        int id = in.readInt();
                        int x = in.readInt();
                        int y = in.readInt();
                        if (changed)
                            Platform.runLater(() -> listener.onBoxChange(id, x, y));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throwError(e);
        }
    }

    public synchronized void move(int direction) {
        try {
            out.writeInt(CLIENT_MOVE);
            out.writeInt(direction);
        } catch (Exception e) {
            throwError(e);
        }
    }

    public synchronized void throwPropaganda(int direction) {
        try {
            out.writeInt(CLIENT_THROW);
            out.writeInt(direction);
        } catch (Exception e) {
            throwError(e);
        }
    }
}