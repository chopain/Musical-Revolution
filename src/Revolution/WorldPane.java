package Revolution;

import bases.CommunistBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import objects.Box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldPane extends Pane {
    private static List<ImageView> bases = Collections.synchronizedList(new ArrayList<ImageView>());
    private static List<Shape> borders = Collections.synchronizedList(new ArrayList<Shape>());
    private static ImageView background = new ImageView(new Image("gamebg.jpg", 1400, 0, true, true));
    private static CommunistBase cBase = new CommunistBase();


    public WorldPane() {
    }

    public void setShapes(List<ImageView> players, List<ImageView> plebians, List<ImageView> propaganda, List<Shape> walls, ScorePane scores) {
        this.getChildren().clear();
        this.getChildren().add(background);
        this.getChildren().add(cBase.getFace());
        this.getChildren().addAll(walls);
        this.getChildren().addAll(players);
        this.getChildren().addAll(plebians);
        this.getChildren().add(scores);
        this.getChildren().addAll(propaganda);
    }

    public void setLoading(ImageView bg) {
        this.getChildren().clear();
        this.getChildren().add(bg);
    }

}
