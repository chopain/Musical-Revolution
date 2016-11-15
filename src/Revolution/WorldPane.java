package Revolution;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.util.List;

public class WorldPane extends Pane {
    public WorldPane() {
    }

    public void setShapes(ImageView bg, List<ImageView> players, List<ImageView> plebians, List<ImageView> bases, List<Shape> borders) {
        this.getChildren().clear();
        this.getChildren().add(bg);
        this.getChildren().addAll(players);
        this.getChildren().addAll(plebians);
        this.getChildren().addAll(bases);
        this.getChildren().addAll(borders);

    }

    public void setLoading(ImageView bg){
        this.getChildren().clear();
        this.getChildren().add(bg);
    }

}
