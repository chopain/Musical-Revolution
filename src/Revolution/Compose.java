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
import objects.Composition;
import people.*;

import java.util.*;

import static Revolution.MoveType.*;

public class Compose extends Application {
    // booleans for key presses
    private boolean pressedDown, pressedUp, pressedLeft, pressedRight, pressedShift;

    @Override
    public void start(Stage world) throws Exception {
        WorldPane root = new WorldPane();
        ImageView loading = new ImageView(new Image("startbg.jpg", 1400, 0, true, true));
        root.setLoading(loading);

        HandleChanges changeListener = new HandleChanges(root);
        Gateway gateway = new Gateway(changeListener);

        //Game start dialog
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogInDocument.fxml"));
        Parent logroot = loader.load();
        Stage dialog = new Stage();
        dialog.setScene(new Scene(logroot));
        dialog.setTitle("Enter Game");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        FXMLLogInDocumentController controller = loader.getController();
        controller.setGateway(gateway, changeListener);
        dialog.setOnCloseRequest(event -> System.exit(0));
        dialog.show();

        // main game window
        world.setTitle("Play.");
        Scene scene;
        world.setScene(scene = new Scene(root, 1400, 750));
        world.setResizable(false);
        world.show();

        world.setOnCloseRequest(e -> System.exit(0));

        // key change listener
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

        // handles moves by the player
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
                        gateway.throwMusic(direction);
                        prevThrow = System.currentTimeMillis();
                    }
                }
                try {
                    Thread.sleep(25);
                } catch (Exception e) {
                }
            }
        }).start();
        dialog.requestFocus();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

// handles changes from the server
class HandleChanges implements Gateway.GatewayListener {
    private final WorldPane world;
    private final ScorePane scores;
    private String handle;
    private final List<ImageView> players = Collections.synchronizedList(new ArrayList<ImageView>());
    private final List<ImageView> npc = Collections.synchronizedList(new ArrayList<ImageView>());
    private final List<ImageView> compositions = Collections.synchronizedList(new ArrayList<ImageView>());
    private final List<Shape> walls = Collections.synchronizedList(new ArrayList<Shape>());
    private final List<Npc> npcObjects = Collections.synchronizedList(new ArrayList<Npc>());
    private final HashMap<Integer, Composition> compositionObjects = new HashMap<>();
    private final HashMap<Integer, Composer> composerObjects = new HashMap<>();

    public HandleChanges(WorldPane world) {
        this.world = world;
        scores = new ScorePane();
    }

    public void setUser(String user) {
        this.handle = user;
    }

    private void updateWorld() {
        world.setShapes(players, npc, compositions, walls, scores);
    }

    @Override
    public void onBoxChange(int id, int x, int y) {
        npcObjects.stream().filter(b -> b.id == id).forEachOrdered(b -> b.move(x, y));
    }

    // set up world representation
    @Override
    public void onInitialized(int width, int height, int score0, int score1, Box me) {
        scores.setScores(1, score1);
        scores.setScores(0, score0);
        updateWorld();
    }

    @Override
    public void onBoxesSet(Box[] npcs, Box[] walls) {
        //create npc from Npc image
        for (Box b : npcs) {
            Npc p = new Npc(b.boxid);
            npcObjects.add(p);
            p.move(b.getX(), b.getY());
            npc.add(p.getFace());
        }
        //add base walls to be displayed from Box objects
        for (Box b : walls) {
            this.walls.add(b.getShape());
        }
    }

    @Override
    public void onBallChange(int id, int ox, int oy) {
        //if composition with that id exists, update the composition, otherwise create new object
        boolean contains = false;
        for (Composition p : compositionObjects.values()) {
            if (p.getId() == id) {
                p.setX(ox);
                p.setY(oy);
                contains = true;
                break;
            }
        }
        if (!contains) {
            //create the object
            compositionObjects.put(id, new Composition(id, ox, oy));
            compositions.add(compositionObjects.get(id).getShape());
            updateWorld();
        }
    }

    @Override
    public void onClientChange(int id, int party, String name, int x, int y, int pCount, boolean enteredFactory) {
        // adds a player if new, otherwise updates the player's location
        boolean contains = false;
        if (!composerObjects.isEmpty()) {
            for (Composer p : composerObjects.values()) {
                if (p.getID() == id) {
                    p.move(x, y);
                    p.setPeriod(party);
                    if (p.getName().equals(handle)) {
                        scores.setpCount(pCount);
                    }
                    contains = true;
                    break;
                }
            }
        }

        if (!contains) {
            composerObjects.put(id, new Composer(id, party, name, x, y));
            players.add(composerObjects.get(id).getFace());
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
        for (Iterator<Map.Entry<Integer, Composition>> i = compositionObjects.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<Integer, Composition> p = i.next();
            if (p.getValue().getId() == id) {
                compositions.remove(p.getValue().getShape());
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
        for (Iterator<Map.Entry<Integer, Composer>> i = composerObjects.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<Integer, Composer> p = i.next();
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


