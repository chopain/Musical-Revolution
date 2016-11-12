package Revolution;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class WorldPane extends Pane {
    public WorldPane() {
        
    }
    
    public void setShapes(List<ImageView> newShapes) {
        this.getChildren().clear();
        this.getChildren().addAll(newShapes);
    }
}
