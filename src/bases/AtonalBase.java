package bases;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AtonalBase implements PixelMeasures {

    private final ImageView face = new ImageView();

    public AtonalBase() {
        this.face.setImage(new Image("schoenberg.jpeg", 600, 310, true, true));
        this.face.setX(-40);
        this.face.setY(WORLD_HEIGHT - BASE_HEIGHT-10);
    }

    public ImageView getFace() {
        return face;
    }
}
