package Revolution;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import people.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

import static Revolution.MoveType.*;

public class Commemeism extends Application {

    List<ImageView> players = new ArrayList<>();
    List<ImageView> plebians = new ArrayList<>();


    @Override
    public void start(Stage world) throws Exception {
        WorldPane root = new WorldPane();
        TreeMap<String, propagandist> playerObjects = new TreeMap<>();
        players.add(new propagandist("communist", "name").getFace());
        CommemeismGateway gateway = new CommemeismGateway(root);
        root.setShapes(players);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogInDocument.fxml"));
        Parent logroot = loader.load();
        Stage dialog = new Stage();
        dialog.setScene(new Scene(logroot));
        dialog.setTitle("Enter Game");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        FXMLLogInDocumentController controller = (FXMLLogInDocumentController) loader.getController();
        controller.setGateway(gateway);
        controller.setWorld(root);
        controller.setMap(playerObjects);
        dialog.setOnCloseRequest(event -> System.exit(0));
        dialog.show();


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

class PlayerCheck implements Runnable {
    CommemeismGateway gateway;
    TreeMap<String, propagandist> players;
    WorldPane world;
    ReentrantLock lock = new ReentrantLock();
    int N = 0;

    public PlayerCheck(CommemeismGateway gate, TreeMap<String, propagandist> players, WorldPane w) {
        this.gateway = gate;
        this.players = players;
        this.world = w;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (gateway.getPlayerCount() > N) {
                try {
                    players.put(gateway.getPlayerHandle(N), new propagandist(gateway.getPlayerHandle(N), gateway.getPlayerType(N)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                N++;
                lock.unlock();
            } else {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                }
            }
        }
    }


}

class GameCheck implements Runnable {
    CommemeismGateway gateway;
    TreeMap<String, propagandist> players;
    WorldPane world;
    String handle;
    int N = 0;
    ReentrantLock lock = new ReentrantLock();

    public GameCheck(CommemeismGateway gateway, WorldPane world, String text, TreeMap<String, propagandist> players) {
        this.gateway = gateway;
        this.world = world;
        this.handle = text;
        this.players = players;
        new Thread(new PlayerCheck(this.gateway, this.players, this.world)).start();
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (gateway.getMoveCount() > N) {
                Platform.runLater(() -> {
                    try {
                        gateway.getMoves(N, players);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                N++;
                lock.unlock();
            } else {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}

