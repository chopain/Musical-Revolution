package Revolution;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import people.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static Revolution.MoveType.*;

public class Commemeism extends Application {

    List<ImageView> players = new ArrayList<>();
    List<ImageView> plebians = new ArrayList<>();


    @Override
    public void start(Stage world) throws Exception {
        WorldPane root = new WorldPane();
        players.add(new propagandist("communist", "name").getFace());
        CommemeismGateway gateway = new CommemeismGateway(root);
        root.setShapes(players);

        world.setTitle("Hello World, prepare for the Proletariat Uprising.");
        world.setScene(new Scene(root, 1700, 1000));
        world.setResizable(false);
        world.show();
        root.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:
                    gateway.move(MOVEUP);
                    break;
                case DOWN:
                    gateway.move(MOVEDOWN);
                    break;
                case LEFT:
                    gateway.move(MOVELEFT);
                    break;
                case RIGHT:
                    gateway.move(MOVERIGHT);
                    break;
                case SHIFT:
                    gateway.move(THROW);
                    break;
            }
        });
        root.requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

class GameCheck implements Runnable {
    CommemeismGateway gateway;
    WorldPane world;
    String handle;
    int N = 0;
    ReentrantLock lock = new ReentrantLock();

    public GameCheck(CommemeismGateway gateway, WorldPane world, String text) {
        this.gateway = gateway;
        this.world = world;
        this.handle = text;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (gateway.getMoveCount() > N) {
                //Platform.runLater(() -> world.setShapes(gateway.getMoves(N)));
                N++;
                lock.unlock();
            }
            else {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}

