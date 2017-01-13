package Revolution;

import javafx.application.Platform;
import objects.Box;

import java.io.*;
import java.net.Socket;


class Gateway extends Thread implements MessageCodes {
    private DataInputStream in;
    private DataOutputStream out;
    private final GatewayListener listener;


    // Establish the connection to the server.
    public Gateway(GatewayListener listen) throws Exception {

        listener = listen;
    }

    /*sends player's name and side to the server
    and gets the current game layout*/
    public void setFields(String host, int port, String name, int side) throws IOException {


        // Create a socket to connect to the server
        Socket socket = new Socket(host, port);


        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        out.writeUTF(name);
        out.writeInt(side);

        listener.onInitialized(in.readInt(), in.readInt(), in.readInt(), in.readInt(),
                new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt()));

        Box[] walls = new Box[in.readInt()];
        for (int i = 0; i < walls.length; i++) {
            walls[i] = new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt());
        }

        Box[] npcs = new Box[in.readInt()];
        for (int i = 0; i < npcs.length; i++) {
            int id = in.readInt();
            npcs[i] = new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt());
            npcs[i].boxid = id;
        }

        listener.onBoxesSet(npcs, walls);

        int players = in.readInt();
        for (int i = 0; i < players; i++)
            listener.onClientChange(in.readInt(), in.readInt(), in.readUTF(), in.readInt(), in.readInt(), in.readInt(), in.readBoolean());
    }


    public interface GatewayListener {
        void onInitialized(int width, int height, int score0, int score1, Box me);

        void onBoxesSet(Box[] npcs, Box[] walls);

        void onBallChange(int id, int ox, int oy);

        void onBoxChange(int id, int x, int y);

        void onClientChange(int id, int party, String name, int x, int y, int c, boolean b);

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
                        int propagandaCount = in.readInt();
                        boolean enterFactory = in.readBoolean();
                        if (changed)
                            Platform.runLater(() -> listener.onClientChange(id, party, name, originX, originY, propagandaCount, enterFactory));
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

    public synchronized void throwMusic(int direction) {
        try {
            out.writeInt(CLIENT_THROW);
            out.writeInt(direction);
        } catch (Exception e) {
            throwError(e);
        }
    }
}