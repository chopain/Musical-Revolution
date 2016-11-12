package Revolution;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import sun.print.DialogOwner;

import java.util.ArrayList;
import java.util.List;

import static Revolution.MoveType.*;

public class Commemeism extends Application {

    List<ImageView> players = new ArrayList<>();
    List<ImageView> plebians = new ArrayList<>();


    @Override
    public void start(Stage world) throws Exception {
        WorldPane root = new WorldPane();
        CommemeismGateway gateway = new CommemeismGateway(root);

        world.setTitle("Hello World, prepare for the Proletariat Uprising.");
        world.setScene(new Scene(root, 1200, 800));
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
    public GameCheck() {

    }

    @Override
    public void run() {

    }
}

