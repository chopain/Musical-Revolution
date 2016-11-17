package Revolution;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import objects.Box;
import objects.Propaganda;
import people.plebian;
import people.propagandist;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.TreeMap;

import static Revolution.MoveType.*;

public class CommemeismGateway extends Thread{

    private static final int CLIENT_MOVE = 1;
    private static final int CLIENT_THROW = 2;

    private static final int SERVER_CHANGED_BALL = 1;
    private static final int SERVER_CHANGED_CLIENT = 2;
    private static final int SERVER_CHANGED_SCORE = 3;

    private final DataInputStream in;
    private final DataOutputStream out;
    private final GatewayListener listener;

    private WorldPane world;

    // Establish the connection to the server.
    public CommemeismGateway(WorldPane world, String name, int side, GatewayListener l) throws Exception{
        this.world = world;
        // Create a socket to connect to the server
        Socket socket = new Socket("localhost", 8000);

        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        listener = l;

        out.writeUTF(name);
        out.writeInt(side);

        listener.onInitialized(in.readInt(), in.readInt(), in.readInt(), in.readInt(),
                new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt(), false));

        Box[] walls = new Box[in.readInt()];
        for(int i = 0; i < walls.length; i++)
            walls[i] = new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt(), false);
        Box[] voters = new Box[in.readInt()];
        for(int i = 0; i < voters.length; i++)
            voters[i] = new Box(in.readInt(), in.readInt(), in.readInt(), in.readInt(), false);

        listener.onBoxesSet(voters, walls);

        int players = in.readInt();
        for(int i = 0; i < players; i++)
            listener.onClientChange(in.readInt(), in.readInt(), in.readUTF(), in.readInt(), in.readInt(), in.readInt(), in.readInt());

    }

    public interface GatewayListener{
        void onInitialized(int width, int height, int score0, int score1, Box me);
        void onBoxesSet(Box[] voters, Box[] walls);
        void onBallChange(int id, int ox, int oy, int dx, int dy);
        void onClientChange(int id, int size,String name, int x, int y, int width, int height);
        void onScoreChange(int team, int score);
        void onBallRemove(int id);
        void onClientRemove(int id);
        void onError(Exception e);
    }

    private boolean error = false;

    private synchronized void throwError(Exception e){
        if(error)
            return;
        error = true;
        listener.onError(e);
    }

    @Override
    public void run(){
        try {
            while (true) {
                boolean changed = in.readBoolean();
                switch (in.readInt()){
                    case SERVER_CHANGED_BALL: {
                        int id = in.readInt();
                        int originX = in.readInt();
                        int originY = in.readInt();
                        int dX = in.readInt();
                        int dY = in.readInt();
                        if(changed)
                            listener.onBallChange(id, originX, originY, dX, dY);
                        else
                            listener.onBallRemove(id);
                        break;
                    }
                    case SERVER_CHANGED_CLIENT: {
                        int id = in.readInt();
                        int side = in.readInt();
                        String name = in.readUTF();
                        int originX = in.readInt();
                        int originY = in.readInt();
                        int w = in.readInt();
                        int h = in.readInt();
                        if(changed)
                            listener.onClientChange(id, side, name, originX, originY, w, h);
                        else
                            listener.onClientRemove(id);
                        break;
                    }
                    case SERVER_CHANGED_SCORE: {
                        int team = in.readInt();
                        int score = in.readInt();
                        listener.onScoreChange(team, score);
                        break;
                    }
                }
            }
        }catch (Exception e){
            throwError(e);
        }
    }

    public synchronized void move(int direction){
        try {
            out.writeInt(CLIENT_MOVE);
            out.writeInt(direction);
        }catch(Exception e){
            throwError(e);
        }
    }

    public synchronized void throwP(int direction){
        try {
            out.writeInt(CLIENT_THROW);
            out.writeInt(direction);
        }catch(Exception e){
            throwError(e);
        }
    }
}