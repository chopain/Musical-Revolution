package Revolution;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import people.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

import static Revolution.MoveType.*;

public class Commemeism extends Application {
    private List<ImageView> players = Collections.synchronizedList(new ArrayList<ImageView>());
    private List<ImageView> plebians = Collections.synchronizedList(new ArrayList<ImageView>());
    private List<ImageView> objects = Collections.synchronizedList(new ArrayList<ImageView>());
    private List<Shape> someShapes = Collections.synchronizedList(new ArrayList<Shape>());


    @Override
    public void start(Stage world) throws Exception {
        WorldPane root = new WorldPane();
        TreeMap<String, propagandist> playerObjects = new TreeMap<>();
        CommemeismGateway gateway = new CommemeismGateway(root);
        ImageView loading = new ImageView(new Image("gamebg_load.jpg", 1400, 0, true, true));
        root.setLoading(loading);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogInDocument.fxml"));
        Parent logroot = loader.load();
        Stage dialog = new Stage();
        dialog.setScene(new Scene(logroot));
        dialog.setTitle("Enter Game");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        FXMLLogInDocumentController controller = loader.getController();
        controller.setGateway(gateway);
        controller.setWorld(root, players, plebians, objects,someShapes, playerObjects);
        dialog.setOnCloseRequest(event -> System.exit(0));
        dialog.show();

        world.setTitle("Hello World, prepare for the Proletariat Uprising.");
        world.setScene(new Scene(root, 1400, 750));
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

class PlebianCheck implements Runnable {
    CommemeismGateway gateway;
    List<plebian> plebians;
    List<ImageView> plebs;
    List<ImageView> bases;
    List<Shape> borders;
    List<ImageView> propagandists;
    WorldPane world;
    ReentrantLock lock = new ReentrantLock();
    ImageView background = new ImageView(new Image("gamebg.jpg", 1400, 0, true, true));
    int N = 0;

    public PlebianCheck(CommemeismGateway gate, List<plebian> plebs, WorldPane w, List<ImageView> plebImages) {
        this.gateway = gate;
        this.plebians = plebs;
        this.plebs = plebImages;
        this.world = w;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (gateway.getPlebianCount() > N) {
                try {
                    gateway.getPlebian(N, plebians, plebs);
                    world.setShapes(background, propagandists, plebs, bases, borders);
                    System.out.println("plebian added");
                    N++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

class PlayerCheck implements Runnable {
    CommemeismGateway gateway;
    TreeMap<String, propagandist> players;
    List<ImageView> propagandists;
    List<plebian> plebians;
    List<ImageView> plebs;
    List<ImageView> bases;
    List<Shape> borders;
    WorldPane world;
    ReentrantLock lock = new ReentrantLock();
    int N = 0;

    public PlayerCheck(CommemeismGateway gate, TreeMap<String, propagandist> players, WorldPane w, List<ImageView> playerImages,
                       List<ImageView> plebs, List<plebian> plebians, List<ImageView> bases, List<Shape> borders) {
        this.gateway = gate;
        this.players = players;
        this.propagandists = playerImages;
        this.world = w;
        this.plebians = plebians;
        this.plebs = plebs;
        this.bases = bases;
        this.borders = borders;
    }

    public void setWorld(WorldPane world, List<ImageView> p, TreeMap<String, propagandist> players) {
        this.world = world;
        this.propagandists = p;
        this.players = players;

    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (gateway.getPlayerCount() > N) {
                try {
                    players.put(gateway.getPlayerHandle(N), new propagandist(gateway.getPlayerHandle(N), gateway.getPlayerType(N)));
                    propagandists.add(players.get(gateway.getPlayerHandle(N)).getFace());
                    //world.setShapes(propagandists);
                    System.out.println("player added");
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
    private List<ImageView> projections;
    CommemeismGateway gateway;
    TreeMap<String, propagandist> players;
    WorldPane world;
    String handle;
    int N = 0;
    ReentrantLock lock = new ReentrantLock();

    public GameCheck(CommemeismGateway gateway, WorldPane world, String text, TreeMap<String, propagandist> players, List<ImageView> project) {
        this.gateway = gateway;
        this.world = world;
        this.handle = text;
        this.players = players;
        this.projections = project;
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

