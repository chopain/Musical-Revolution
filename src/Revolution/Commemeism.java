package Revolution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import objects.Box;
import objects.Propaganda;
import people.*;
import java.util.*;
import static Revolution.MoveType.*;

public class Commemeism extends Application {
    private boolean pressedDown, pressedUp, pressedLeft, pressedRight, pressedShift;

    @Override
    public void start(Stage world) throws Exception {
        WorldPane root = new WorldPane();
        ImageView loading = new ImageView(new Image("gamebg_load.jpg", 1400, 0, true, true));
        root.setLoading(loading);

        HandleChanges changeListener = new HandleChanges(root, 1400, 750);
        CommemeismGateway gateway = new CommemeismGateway(changeListener);

        //Game start dialog
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogInDocument.fxml"));
        Parent logroot = loader.load();
        Stage dialog = new Stage();
        dialog.setScene(new Scene(logroot));
        dialog.setTitle("Enter Game");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        FXMLLogInDocumentController controller = loader.getController();
        controller.setGateway(gateway);
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
            long prevThrow = 0;
            int direction = MOVEUP;
            while (true) {
                if (pressedUp) gateway.move(direction = MOVEUP);
                if (pressedDown) gateway.move(direction = MOVEDOWN);
                if (pressedLeft) gateway.move(direction = MOVELEFT);
                if (pressedRight) gateway.move(direction = MOVERIGHT);
                if (pressedShift) {
                    if (prevThrow + 200 < System.currentTimeMillis()) {
                        System.out.print("attempt shoot");
                        gateway.throwPropaganda(direction);
                        prevThrow = System.currentTimeMillis();
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

class HandleChanges implements CommemeismGateway.GatewayListener {
    private double width;
    private double height;
    private WorldPane world;
    private ScorePane scores;

    private List<ImageView> players = Collections.synchronizedList(new ArrayList<ImageView>());
    private List<ImageView> plebians = Collections.synchronizedList(new ArrayList<ImageView>());
    private List<ImageView> propaganda = Collections.synchronizedList(new ArrayList<ImageView>());
    private List<Shape> walls = Collections.synchronizedList(new ArrayList<Shape>());
    private List<plebian> plebianObjects = Collections.synchronizedList(new ArrayList<plebian>());
    private HashMap<Integer, Propaganda> propagandaObjects = new HashMap<>();
    private HashMap<Integer, propagandist> propagandistObjects = new HashMap<>();

    public HandleChanges(WorldPane world, double width, double height) {
        this.width = width;
        this.height = height;
        this.world = world;
        scores = new ScorePane();
    }

    private void updateWorld() {
        world.setShapes(players, plebians, propaganda, walls, scores);
    }

    @Override
    public void onBoxChange(int id, int x, int y) {
        for(plebian b : plebianObjects){
            if(b.id == id)
                b.move(x, y);
        }
    }

    @Override
    public void onInitialized(int width, int height, int score0, int score1, Box me) {
        Box worldEdges = new Box(0, 0, width, height);
        scores.setScores(1, score1);
        scores.setScores(0, score0);
        updateWorld();
    }

    @Override
    public void onBoxesSet(Box[] voters, Box[] walls) {
        //create plebians from plebian image
        for (Box b : voters) {
            plebian p = new plebian(b._id);
            plebianObjects.add(p);
            p.move(b.getX(), b.getY());
            plebians.add(p.getFace());
        }
        //add base walls to be displayed from Box objects
        for (Box b : walls) {
            this.walls.add(b.getShape());
        }
    }

    @Override
    public void onBallChange(int id, int ox, int oy) {
        //if propaganda with that id exists, edit the propaganda, otherwise create new object
        boolean contains = false;
        for (Propaganda p : propagandaObjects.values()) {
            if (p.getId() == id) {
                p.setX(ox);
                p.setY(oy);
                contains = true;
                break;
            }
        }
        if (!contains) {
            //create the object
            propagandaObjects.put(id, new Propaganda(id, ox, oy));
            propaganda.add(propagandaObjects.get(id).getShape());
            updateWorld();
        }
    }

    @Override
    public void onClientChange(int id, int party, String name, int x, int y, int pCount, boolean enteredFactory) {
        boolean contains = false;
        if (!propagandistObjects.isEmpty()) {
            for (propagandist p : propagandistObjects.values()) {
                if (p.getID() == id) {
                    p.move(x, y);
                    contains = true;
                    break;
                }
            }
        }

        scores.setpCount(pCount);
        if (!contains) {
            propagandistObjects.put(id, new propagandist(id, party, name, x, y));
            players.add(propagandistObjects.get(id).getFace());
            updateWorld();
        }
    }

    @Override
    public void onScoreChange(int team, int score) {
        System.out.println("Score: " + team + ": " + score);
        scores.setScores(team, score);
        updateWorld();
    }

    @Override
    public void onBallRemove(int id) {
        boolean changed = false;
        for (Iterator<Map.Entry<Integer, Propaganda>> i = propagandaObjects.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<Integer, Propaganda> p = i.next();
            if (p.getValue().getId() == id) {
                propaganda.remove(p.getValue().getShape());
                i.remove();
                changed = true;
                break;
            }
        }
        if (changed)
            updateWorld();
    }

    @Override
    public void onClientRemove(int id) {
        boolean changed = false;
        for (Iterator<Map.Entry<Integer, propagandist>> i = propagandistObjects.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<Integer, propagandist> p = i.next();
            if (p.getValue().getID() == id) {
                players.remove(p.getValue().getFace());
                i.remove();
                changed = true;
                break;
            }
        }
        if (changed)
            updateWorld();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        System.out.println("lost connection");
        System.exit(0);
    }
}


