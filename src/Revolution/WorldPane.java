package Revolution;

import bases.CommunistBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.List;

public class WorldPane extends Pane {
    private List<ImageView> bases;
    private List<Shape> borders;
    private static ImageView background = new ImageView(new Image("gamebg.jpg", 1400, 0, true, true));
    private static CommunistBase cBase = new CommunistBase();

    public WorldPane(List<ImageView> base, List<Shape> border) {
        this.bases = base;
        this.borders = border;
    }

    public void setShapes(List<ImageView> players, List<ImageView> plebians, ScorePane scores) {
        this.getChildren().clear();
        this.getChildren().add(background);
        this.getChildren().add(cBase.getFace());
        this.getChildren().addAll(cBase.getBorder());
        this.getChildren().addAll(players);
        this.getChildren().addAll(plebians);
        this.getChildren().add(scores);
        this.getChildren().addAll(bases);
        this.getChildren().addAll(borders);

    }

    public void setLoading(ImageView bg) {
        this.getChildren().clear();
        this.getChildren().add(bg);
    }

}
