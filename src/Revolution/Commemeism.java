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
import objects.Box;
import objects.Propaganda;
import people.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

import static Revolution.MoveType.*;

public class Commemeism extends Application implements CommemeismGateway.GatewayListener{
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
                        System.out.print("attempt shoot");
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

    @Override
    public void onInitialized(int width, int height, int score0, int score1, Box me) {
        Box world = new Box(0,0, width, height, false);

    }

    @Override
    public void onBoxesSet(Box[] voters, Box[] walls) {

    }

    @Override
    public void onBallChange(int id, int ox, int oy, int dx, int dy) {

    }

    @Override
    public void onClientChange(int id, int size, String name, int x, int y, int width, int height) {
        // if the client with "id" already exists, only edit x and y
        // otherwise create the player itself
    }

    @Override
    public void onScoreChange(int team, int score) {

    }

    @Override
    public void onBallRemove(int id) {

    }

    @Override
    public void onClientRemove(int id) {

    }

    @Override
    public void onError(Exception e) {

    }
}


