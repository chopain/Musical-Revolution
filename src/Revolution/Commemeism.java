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
import objects.Propaganda;
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
    private List<ImageView> propaganda = Collections.synchronizedList(new ArrayList<ImageView>());
    private List<Shape> someShapes = Collections.synchronizedList(new ArrayList<Shape>());
    private List<plebian> plebianObjects = Collections.synchronizedList(new ArrayList<plebian>());

    private boolean pressedDown, pressedUp, pressedLeft, pressedRight, pressedShift;

    @Override
    public void start(Stage world) throws Exception {
        WorldPane root = new WorldPane(objects, someShapes);
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
        controller.setWorld(root, players, plebians, plebianObjects, playerObjects);
        dialog.setOnCloseRequest(event -> System.exit(0));
        dialog.show();

        world.setTitle("Hello World, prepare for the Proletariat Uprising.");
        Scene scene;
        world.setScene(scene = new Scene(root, 1400, 750));
        world.setResizable(false);
        world.show();

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:
                    pressedUp = true;
                    break;
                case DOWN:
                    pressedDown = true;
                    break;
                case LEFT:
                    pressedLeft = true;
                    break;
                case RIGHT:
                    pressedRight = true;
                    break;
                case SHIFT:
                    pressedShift = true;
                    break;
            }
        });
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case UP:
                    pressedUp = false;
                    break;
                case DOWN:
                    pressedDown = false;
                    break;
                case LEFT:
                    pressedLeft = false;
                    break;
                case RIGHT:
                    pressedRight = false;
                    break;
                case SHIFT:
                    pressedShift = false;
                    break;
            }
        });

        new Thread(() -> {
            long lastthrow = 0;
            while (true) {
                if (pressedUp) gateway.move(MOVEUP);
                if (pressedDown) gateway.move(MOVEDOWN);
                if (pressedLeft) gateway.move(MOVELEFT);
                if (pressedRight) gateway.move(MOVERIGHT);
                if (pressedShift) {
                    if (lastthrow + 200 < System.currentTimeMillis()) {
                        gateway.move(THROW);
                        lastthrow = System.currentTimeMillis();
                    }
                }
                try {
                    Thread.sleep(25);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class PlebianCheck implements Runnable {
    private CommemeismGateway gateway;
    private List<plebian> plebians;
    private List<ImageView> plebs;
    private List<ImageView> propagandists;
    private List<ImageView> propaganda;
    private WorldPane world;
    private ReentrantLock lock = new ReentrantLock();
    private int N = 0;
    ScorePane scorePane = new ScorePane();

    public PlebianCheck(CommemeismGateway gate, List<plebian> plebs, WorldPane w, List<ImageView> plebImages, List<ImageView> players, List<ImageView> propaganda, ScorePane scores) {
        this.gateway = gate;
        this.plebians = plebs;
        this.plebs = plebImages;
        this.propagandists = players;
        this.world = w;
        this.scorePane = scores;
        this.propaganda = propaganda;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (gateway.getPlebianCount() > N) {
                try {
                    gateway.getPlebian(N, plebians, plebs);
                    Platform.runLater(() -> world.setShapes(propagandists, plebs, propaganda, scorePane));
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

class ScoreCheck implements Runnable {
    CommemeismGateway gateway;
    ScorePane scorePane;
    private List<ImageView> plebs;
    private List<ImageView> propagandists;
    private List<ImageView> propaganda;
    private WorldPane world;

    public ScoreCheck(CommemeismGateway gate, WorldPane w, List<ImageView> plebImages,
                      List<ImageView> players, List<ImageView> propaganda, ScorePane scores) {
        this.gateway = gate;
        this.plebs = plebImages;
        this.propagandists = players;
        this.world = w;
        this.scorePane = scores;
        this.propaganda = propaganda;
    }

    @Override
    public void run() {
        while (true) {
            try {
                gateway.checkScore(scorePane);
                Platform.runLater(() -> world.setShapes(propagandists, plebs, propaganda, scorePane));
                Thread.sleep(250);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}


class PropagandaCheck implements Runnable {
    private CommemeismGateway gateway;
    private List<plebian> plebians;
    private List<ImageView> plebs;
    private List<ImageView> propaganda;
    private List<ImageView> propagandists;
    private List<Propaganda> propagandaObjects;
    private ScorePane scorePane;
    private WorldPane world;
    private ReentrantLock lock = new ReentrantLock();
    private int N = 0;

    public PropagandaCheck(CommemeismGateway gate, List<plebian> plebs, WorldPane w, List<ImageView> plebImages,
                           List<ImageView> players, List<ImageView> propaganda, List<Propaganda> propagandaObjects, ScorePane scores) {
        this.gateway = gate;
        this.plebians = plebs;
        this.plebs = plebImages;
        this.propagandists = players;
        this.world = w;
        this.propaganda = propaganda;
        this.propagandaObjects = propagandaObjects;
        this.scorePane = scores;
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (gateway.getPropagandaCount() > N) {
                try {
                    gateway.getPropaganda(N, propagandaObjects, propaganda);
                    int N_temp = N;
                    Platform.runLater(() -> world.setShapes(propagandists, plebs, propaganda, scorePane));
                    System.out.println("propaganda added");
                    N++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    private CommemeismGateway gateway;
    private TreeMap<String, propagandist> players;
    private List<ImageView> propagandists;
    private List<plebian> plebians;
    private List<ImageView> plebs;
    private List<ImageView> propaganda;
    private ScorePane scorePane;
    private WorldPane world;
    private ReentrantLock lock = new ReentrantLock();
    private int N = 0;

    public PlayerCheck(CommemeismGateway gate, TreeMap<String, propagandist> players, WorldPane w,
                       List<ImageView> playerImages, List<ImageView> plebs, List<plebian> plebians, List<ImageView> propaganda, ScorePane scores) {
        this.gateway = gate;
        this.players = players;
        this.propagandists = playerImages;
        this.world = w;
        this.plebs = plebs;
        this.plebians = plebians;
        this.propaganda = propaganda;
        this.scorePane = scores;
    }

    @Override
    public void run() {
        while (true) {
            if (gateway.getPlayerCount() > N) {
                try {
                    scorePane.setScores(222, 300, 24);
                    String username = gateway.getPlayerHandle(N);
                    players.put(username, new propagandist(username, gateway.getPlayerType(N)));
                    propagandists.add(players.get(gateway.getPlayerHandle(N)).getFace());
                    gateway.getPlayerPos(N, players.get(username));
                    Platform.runLater(() -> {
                        world.setShapes(propagandists, plebs, propaganda, scorePane);
                    });
                    System.out.println("player added");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                N++;

            } else {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

class GameCheck implements Runnable {
    private List<ImageView> projections;
    private CommemeismGateway gateway;
    private TreeMap<String, propagandist> players;
    private WorldPane world;
    private String handle;
    private int N = 0;

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
            if (gateway.getMoveCount() > N) {
                int N_temp = N;
                Platform.runLater(() -> {
                    try {
                        gateway.getMoves(N_temp, players);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                N++;
            } else {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}

